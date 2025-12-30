document.addEventListener('DOMContentLoaded', () => {
    // --- Selectors ---
    const loginForm = document.getElementById('login-form');
    const loginSection = document.getElementById('login-section');
    const dashboardSection = document.getElementById('dashboard-section');
    const logoutBtn = document.getElementById('logout-btn');
    const userIdInput = document.getElementById('username');

    // View Management
    const menuItems = document.querySelectorAll('.menu-item[data-view]');
    const views = document.querySelectorAll('.view-section');

    // Lockdown
    const lockdownBtn = document.getElementById('btn-lockdown');
    const unlockBtn = document.getElementById('btn-unlock-system');
    const body = document.body;

    // --- Authentication Logic ---
    loginForm.addEventListener('submit', (e) => {
        e.preventDefault();

        const btn = loginForm.querySelector('button');
        const user = userIdInput.value || 'USER';

        // Button loading state
        const originalText = btn.innerHTML;
        btn.innerHTML = '<span class="status-indicator online"></span> Verifying...';
        btn.style.opacity = '0.8';
        btn.disabled = true;

        // Simulate network request
        setTimeout(() => {
            btn.innerHTML = originalText;
            btn.style.opacity = '1';
            btn.disabled = false;

            // Transition
            loginSection.classList.remove('active-screen');
            setTimeout(() => {
                loginSection.classList.add('hidden-screen');
                dashboardSection.classList.remove('hidden-screen');
                // Trigger reflow
                void dashboardSection.offsetWidth;
                dashboardSection.classList.add('active-screen');

                // Update User Display
                document.getElementById('user-display').textContent = `${user} (SESSION_ACTIVE)`;
            }, 500);
        }, 1200);
    });

    logoutBtn.addEventListener('click', () => {
        dashboardSection.classList.remove('active-screen');
        setTimeout(() => {
            dashboardSection.classList.add('hidden-screen');
            loginSection.classList.remove('hidden-screen');

            void loginSection.offsetWidth;
            loginSection.classList.add('active-screen');

            loginForm.reset();
        }, 500);
    });

    // --- Navigation Logic ---
    menuItems.forEach(item => {
        item.addEventListener('click', (e) => {
            e.preventDefault();

            // 1. Update Menu State
            document.querySelectorAll('.menu-item').forEach(el => el.classList.remove('active'));
            item.classList.add('active');

            // 2. Switch Views
            const targetId = `view-${item.dataset.view}`;

            views.forEach(view => {
                if (view.id === targetId) {
                    view.classList.remove('hidden-view');
                    view.classList.add('active-view');
                } else {
                    view.classList.add('hidden-view');
                    view.classList.remove('active-view');
                }
            });
        });
    });

    // --- Lockdown Logic ---
    lockdownBtn.addEventListener('click', (e) => {
        e.preventDefault();
        // Play alert sound if we had one
        body.classList.add('lockdown-active');
    });

    unlockBtn.addEventListener('click', () => {
        const btn = unlockBtn;
        btn.textContent = "VERIFYING OVERRIDE...";

        setTimeout(() => {
            body.classList.remove('lockdown-active');
            btn.textContent = "AUTHORIZE OVERRIDE";

            // Optional: Reset to overview
            document.querySelector('[data-view="overview"]').click();
        }, 2000);
    });

    // --- API Data Fetching ---

    const fetchData = async () => {
        try {
            // 1. Fetch Stats
            const statsRes = await fetch('/api/stats');
            const stats = await statsRes.json();

            // Update UI
            document.querySelector('.stat-value').textContent = stats.onCampus; // Active/On Campus
            document.querySelector('#security-level-text').textContent = stats.securityLevel;
            document.querySelector('.stat-trend').textContent = stats.defcon;
            // System Load (last card)
            document.querySelectorAll('.stat-value')[3].textContent = stats.load + '%';

            // 2. Fetch Logs
            const logsRes = await fetch('/api/logs');
            const logs = await logsRes.json();
            renderLogs(logs);

            // 3. Fetch Users & Attendance for Personnel View
            const usersRes = await fetch('/api/users');
            const users = await usersRes.json();

            const attendanceRes = await fetch('/api/attendance');
            const attendance = await attendanceRes.json();

            renderPersonnel(users, attendance);

            // 4. Fetch Zones for Zone View
            const zonesRes = await fetch('/api/zones');
            const zones = await zonesRes.json();
            renderZones(zones);

        } catch (err) {
            console.error('Failed to fetch data:', err);
        }
    };

    // Global function to filter logs
    window.filterLogs = async (userId) => {
        // Switch to logs view
        document.querySelector('[data-view="logs"]').click();

        // Update Header
        const header = document.querySelector('#view-logs h2');
        header.innerHTML = `Audit Trail: <span class="highlight">${userId}</span> <button class="btn-small" onclick="clearFilter()" style="margin-left: 10px; font-size: 0.6rem;">Clear</button>`;

        // Fetch filtered
        try {
            const res = await fetch(`/api/logs?user=${userId}`);
            const logs = await res.json();
            renderLogs(logs); // Re-use render function, but this time it updates the table with filtered data
        } catch (e) { console.error(e); }
    };

    window.clearFilter = () => {
        document.querySelector('#view-logs h2').textContent = 'Full Audit Trail';
        fetchData();
    };

    // --- Modal Logic ---
    const modal = document.getElementById('modal-add-user');
    const openModalBtn = document.getElementById('btn-add-personnel');
    const closeModalBtn = document.querySelector('.close-modal');
    const addUserForm = document.getElementById('add-user-form');

    if (openModalBtn) {
        openModalBtn.addEventListener('click', () => {
            modal.classList.remove('hidden-modal');
        });
    }

    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', () => {
            modal.classList.add('hidden-modal');
        });
    }

    if (addUserForm) {
        addUserForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const id = document.getElementById('new-id').value;
            const username = document.getElementById('new-name').value;
            const password = document.getElementById('new-pass').value;
            const role = document.getElementById('new-role').value;

            try {
                const res = await fetch('/api/users', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ id, username, password, role })
                });

                if (res.ok) {
                    alert('User added successfully!');
                    modal.classList.add('hidden-modal');
                    addUserForm.reset();
                    fetchData(); // Refresh list
                } else {
                    alert('Failed to add user.');
                }
            } catch (err) {
                console.error(err);
                alert('Error connecting to server.');
            }
        });
    }

    // --- Render Logic ---

    const renderLogs = (logs) => {
        const logList = document.getElementById('log-list-overview');
        const logsViewTable = document.querySelector('#view-logs tbody');

        // Render Widget List (Top 5)
        if (logList) {
            logList.innerHTML = logs.slice(0, 5).map(log => {
                let tagClass = 'tag-system';
                if (log.message.toLowerCase().includes('admin')) tagClass = 'tag-admin';
                else if (log.message.toLowerCase().includes('access denied')) tagClass = 'tag-auth';

                let time = log.timestamp.split(' ')[1] || log.timestamp;

                return `
                    <li class="log-item ${log.level.toLowerCase() === 'error' ? 'error' : log.level.toLowerCase() === 'warn' ? 'warning' : 'info'}">
                        <span class="time">${time}</span>
                        <span class="log-content"><span class="tag ${tagClass}">${log.level}</span> ${log.message}</span>
                    </li>
                `;
            }).join('');
        }

        // Render Full Table
        if (logsViewTable) {
            logsViewTable.innerHTML = logs.map(log => `
                 <tr>
                    <td class="font-mono">${log.timestamp}</td>
                    <td><span class="tag tag-${log.level.toLowerCase() === 'error' ? 'auth' : 'system'}">${log.level}</span></td>
                    <td>SYSTEM</td>
                    <td>${log.message}</td>
                </tr>
            `).join('');
        }
    };

    const renderPersonnel = (users, attendance) => {
        const tbody = document.querySelector('#view-personnel tbody');
        if (!tbody) return;

        // Create a map of UserID -> Last Attendance Record
        const lastSeen = {};
        attendance.forEach(record => {
            // Assuming simple chronological order in file, or we could sort by timestamp
            lastSeen[record.userId] = record;
        });

        tbody.innerHTML = users.map(user => {
            const lastRecord = lastSeen[user.id];
            const isActive = lastRecord && lastRecord.action === 'CHECK_IN';
            const statusText = isActive ? 'ON SITE' : 'OFF SITE';
            const statusColor = isActive ? 'text-green' : 'text-muted';
            const location = isActive ? 'Campus' : 'Unknown'; // Could refine if we had Zone data in attendance

            return `
            <tr>
                <td>${user.id}</td>
                <td>${user.username}</td>
                <td><span class="tag tag-${user.role === 'ADMIN' ? 'admin' : 'emp'}">${user.role}</span></td>
                <td><span class="${statusColor}">${statusText}</span></td>
                <td>
                    <button class="icon-btn" onclick="filterLogs('${user.id}')" title="View History"><i class="fa-solid fa-clock-rotate-left"></i></button>
                </td>
            </tr>
        `}).join('');
    };

    const renderZones = (zones) => {
        const grid = document.getElementById('zone-grid');
        if (!grid) return;

        // Ensure we handle the view correctly (don't clear if user is clicking buttons inside)
        // But for simplicity in this version, we redraw. To avoid glitching, user feedback happens via alert/toast.

        const zoneIcons = {
            'SERVER_ROOM': 'fa-server',
            'LAB': 'fa-flask',
            'OFFICE_FLOOR': 'fa-briefcase',
            'LOBBY': 'fa-building'
        };

        grid.innerHTML = zones.map(zone => {
            const isLocked = zone.status === 'LOCKED';
            const statusColor = isLocked ? 'text-red' : (zone.status === 'RESTRICTED' ? 'text-gold' : 'text-green');

            return `
            <div class="zone-card-large glass-panel">
                <div class="zone-icon"><i class="fa-solid ${zoneIcons[zone.name] || 'fa-shield-halved'}"></i></div>
                <h3>${zone.name.replace('_', ' ')}</h3>
                <p class="status-text ${statusColor}">${zone.status}</p>
                <div class="zone-controls">
                    <button class="btn-control ${!isLocked ? 'active' : ''} warning" onclick="updateZone('${zone.name}', 'UNLOCKED')">Unlock</button>
                    <button class="btn-control ${isLocked ? 'active' : ''}" onclick="updateZone('${zone.name}', 'LOCKED')">Lock</button>
                </div>
                <div style="margin-top: 1rem; width: 100%; border-top: 1px solid var(--glass-border); padding-top: 0.5rem;">
                    <button class="btn-small" style="width: 100%; font-size: 0.7rem;" onclick="openSimulateModal('${zone.name}')">Test Access</button>
                </div>
            </div>
            `;
        }).join('');
    };

    // Simulation Logic
    window.openSimulateModal = (zoneName) => {
        // Simple prompt for now, could be a custom modal
        const userId = prompt(`Simulate Badge Swipe for ${zoneName}\nEnter User ID (e.g., ADM01, EMP01, VIS01):`);
        if (userId) {
            simulateAccess(userId, zoneName);
        }
    };

    window.simulateAccess = async (userId, zoneName) => {
        try {
            const res = await fetch('/api/access', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ userId, zone: zoneName })
            });
            const data = await res.json();

            // Show result
            if (data.granted) {
                alert(`✅ ACCESS GRANTED\nUser: ${data.role}\nZone: ${data.zone}`);
            } else {
                alert(`❌ ACCESS DENIED\nReason: ${data.message}`);
            }

            fetchData(); // Refresh logs
        } catch (err) {
            console.error(err);
            alert('Simulation failed (Network Error)');
        }
    };

    // Expose updateZone to window so onclick works
    window.updateZone = async (name, status) => {
        try {
            const res = await fetch('/api/zones', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ name, status })
            });
            if (res.ok) {
                // Log the action
                await fetch('/api/logs', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ level: 'WARN', message: `Zone ${name} updated to ${status}` })
                });

                fetchData(); // Refresh UI
            } else {
                alert('Failed to update zone');
            }
        } catch (err) {
            console.error(err);
        }
    };

    // Initial Fetch
    fetchData();
    // Poll every 5 seconds
    setInterval(fetchData, 5000);
});
