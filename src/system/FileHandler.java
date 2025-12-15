package system;

public class FileHandler {
    private void ensureFile(String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Create /data folder if needed
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error ensuring file: " + e.getMessage());
        }
    }
    public List<String> readFile(String path) {
        // Placeholder
        ensureFile(path);
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }

    
    public void writeFile(String path, String content) {
        // Placeholder
        ensureFile(path);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write(content);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
    public void writeLines(String path, List<String> lines) {
        ensureFile(path);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error writing lines: " + e.getMessage());
        }
    }
    public void appendToFile(String path, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path, true))) {

            bw.write(content);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error appending to file: " + path);
        }
    }
}
