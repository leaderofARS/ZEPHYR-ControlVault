const express = require('express');
const fs = require('fs');
const path = require('path');
const cors = require('cors');

const app = express();
const PORT = 5000;

app.use(cors());
app.use(express.json()); // Parse JSON bodies
app.use(express.static(path.join(__dirname, 'public')));

const DATA_DIR = path.join(__dirname, '../data');

// Helper to read file safely
const readFile = (filename) => {
    try {
        if (!fs.existsSync(path.join(DATA_DIR, filename))) return '';
        return fs.readFileSync(path.join(DATA_DIR, filename), 'utf-8');
    } catch (err) {
        console.error(`Error reading ${filename}:`, err);
        return '';
    }
};

// Helper to append to file
const appendFile = (filename, content) => {
    try {
        fs.appendFileSync(path.join(DATA_DIR, filename), '\n' + content);
        return true;
    } catch (err) {
        console.error(`Error writing to ${filename}:`, err);
        return false;
    }
};

// Helper to write (overwrite) file
const writeFile = (filename, content) => {
    try {
        fs.writeFileSync(path.join(DATA_DIR, filename), content);
        return true;
    } catch (err) {
        console.error(`Error overwriting ${filename}:`, err);
        return false;
    }
};

// --- API Endpoints ---

// 1. Users
app.get('/api/users', (req, res) => {
    const data = readFile('users.txt');
    const users = data.trim().split('\n').map(line => {
        const parts = line.split(',');
        if (parts.length < 4) return null;
        return {
            id: parts[0],
            username: parts[1],
            role: parts[3],
            badgeCount: parts[4] ? parseInt(parts[4]) : null
        };
    }).filter(u => u !== null);
    res.json(users);
});

app.post('/api/users', (req, res) => {
    const { id, username, password, role } = req.body;
    if (!id || !username || !password || !role) {
        return res.status(400).json({ error: 'Missing fields' });
    }
    const newLine = `${id},${username},${password},${role},0`;
    if (appendFile('users.txt', newLine)) {
        res.json({ success: true, message: 'User added' });
    } else {
        res.status(500).json({ error: 'Failed to write to file' });
    }
});

// 2. Logs
app.get('/api/logs', (req, res) => {
    const data = readFile('access_log.txt');
    const { user } = req.query; // Get filter query

    const logs = data.trim().split('\n').reverse().map(line => {
        const match = line.match(/^\[(.*?)\] \[(.*?)\] (.*)$/);
        if (!match) return null;
        return {
            timestamp: match[1],
            level: match[2],
            message: match[3],
            original: line
        };
    }).filter(l => l !== null);

    // Filter if user param is present
    if (user) {
        const filtered = logs.filter(log => log.message.includes(user));
        return res.json(filtered);
    }

    res.json(logs);
});

app.post('/api/logs', (req, res) => {
    const { level, message } = req.body;
    const now = new Date();
    // Format: [2025-12-16 21:55:39]
    const timestamp = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0');

    const logLine = `[${timestamp}] [${level}] ${message}`;
    appendFile('access_log.txt', logLine);
    res.json({ success: true });
});

// 3. Attendance
app.get('/api/attendance', (req, res) => {
    const data = readFile('attendance.txt');
    const attendance = data.trim().split('\n').map(line => {
        const parts = line.split(',');
        return {
            timestamp: parts[0],
            userId: parts[1],
            username: parts[2],
            action: parts[3]
        };
    }).filter(a => a.timestamp);
    res.json(attendance);
});

// 4. Zones
app.get('/api/zones', (req, res) => {
    const data = readFile('zones.txt');
    const zones = data.trim().split('\n').map(line => {
        const parts = line.split(',');
        return { name: parts[0], status: parts[1] };
    }).filter(z => z.name);
    res.json(zones);
});

app.post('/api/zones', (req, res) => {
    const { name, status } = req.body;
    const data = readFile('zones.txt');
    let lines = data.trim().split('\n');
    let found = false;

    const newLines = lines.map(line => {
        const parts = line.split(',');
        if (parts[0] === name) {
            found = true;
            return `${name},${status}`;
        }
        return line;
    });

    if (!found) newLines.push(`${name},${status}`);

    if (writeFile('zones.txt', newLines.join('\n'))) {
        res.json({ success: true });
    } else {
        res.status(500).json({ error: 'Failed to update zone' });
    }
});

// 6. Access Simulation (RBAC Engine)
app.post('/api/access', (req, res) => {
    const { userId, zone } = req.body;

    // 1. Fetch User Info
    const usersData = readFile('users.txt');
    const userLine = usersData.split('\n').find(l => l.startsWith(userId + ','));

    // 2. Fetch Zone Info
    const zonesData = readFile('zones.txt');
    const zoneLine = zonesData.split('\n').find(l => l.startsWith(zone + ','));

    if (!userLine) return res.status(404).json({ granted: false, message: 'User not found' });

    const userParts = userLine.split(',');
    const username = userParts[1];
    const role = userParts[3];
    const zoneStatus = zoneLine ? zoneLine.split(',')[1].trim() : 'UNKNOWN';

    let granted = false;
    let denialReason = '';

    // --- ACCESS CONTROL LIST (ACL) ---
    // Rule 1: Master Override
    if (role === 'ADMIN') {
        granted = true;
    }
    // Rule 2: Lockdown / Locked Zone Override
    else if (zoneStatus === 'LOCKED') {
        granted = false;
        denialReason = 'Zone is LOCKED';
    }
    // Rule 3: Role-Specific Access
    else {
        switch (zone) {
            case 'SERVER_ROOM':
                // Only Admin (already handled)
                granted = false;
                denialReason = 'Restricted Content';
                break;
            case 'LAB':
                granted = (role === 'EMPLOYEE');
                if (!granted) denialReason = 'Authorized Personnel Only';
                break;
            case 'OFFICE_FLOOR':
                granted = (role === 'EMPLOYEE');
                if (!granted) denialReason = 'Employees Only';
                break;
            case 'LOBBY':
                granted = true; // Open to Visitors and Employees
                break;
            default:
                granted = false;
                denialReason = 'Unknown Zone';
        }
    }

    // --- LOGGING ---
    const now = new Date();
    const timestamp = now.getFullYear() + '-' +
        String(now.getMonth() + 1).padStart(2, '0') + '-' +
        String(now.getDate()).padStart(2, '0') + ' ' +
        String(now.getHours()).padStart(2, '0') + ':' +
        String(now.getMinutes()).padStart(2, '0') + ':' +
        String(now.getSeconds()).padStart(2, '0');

    let logLine = '';
    if (granted) {
        logLine = `[${timestamp}] [INFO] [${role}] ${username} (ID: ${userId}) accessed ${zone}`;
    } else {
        logLine = `[${timestamp}] [WARN] [${role}] ${username} (ID: ${userId}) denied access to ${zone} (${denialReason})`;
    }

    appendFile('access_log.txt', logLine);

    res.json({
        granted,
        message: granted ? 'Access Granted' : `Access Denied: ${denialReason}`,
        role,
        zone
    });
});

// 7. Dashboard Stats
app.get('/api/stats', (req, res) => {
    // 1. Active Users (Count from attendance check-ins - check-outs currently inside?)
    //    For this simple version, let's just count total users in system or recent logins
    const usersData = readFile('users.txt');
    const logsData = readFile('access_log.txt');

    // Calculate Active Users (Naive: Total configured users)
    const totalUsers = usersData.trim().split('\n').filter(l => l.trim()).length;

    // Calculate "On Campus" (Naive: Last attendance action was CHECK_IN)
    const attendanceData = readFile('attendance.txt');
    const attendanceLines = attendanceData.trim().split('\n');
    const statusMap = {};
    attendanceLines.forEach(line => {
        const parts = line.split(',');
        if (parts.length >= 4) {
            statusMap[parts[1]] = parts[3]; // Overwrites with latest status
        }
    });
    const activeOnCampus = Object.values(statusMap).filter(status => status === 'CHECK_IN').length;

    // Determine Security Level based on recent ERROR logs
    const recentLogs = logsData.trim().split('\n').slice(-10);
    const errorCount = recentLogs.filter(l => l.includes('[ERROR]')).length;
    let securityLevel = 'LOW';
    let defcon = 'DEFCON 5';
    if (errorCount > 0) { securityLevel = 'MODERATE'; defcon = 'DEFCON 3'; }
    if (errorCount > 5) { securityLevel = 'CRITICAL'; defcon = 'DEFCON 1'; }

    res.json({
        activeUsers: totalUsers, // Or specifically "On Campus" count
        onCampus: activeOnCampus,
        securityLevel,
        defcon,
        load: Math.floor(Math.random() * 30) + 10 // Mock load
    });
});

app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
});
