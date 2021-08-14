import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final String SEP = File.separator;

    public static void main(String[] args) {
        System.out.println("Домашнее задание к занятию 1.3\n" +
                "Потоки ввода-вывода. Работа с файлами. Сериализация\n");

        String savePath = "T:" + SEP + "Program Files" + SEP + "Games" + SEP + "savegames";
        GameProgress gp1 = new GameProgress(100, 3, 6, 23.1);
        GameProgress gp2 = new GameProgress(70, 5, 11, 47.3);
        GameProgress gp3 = new GameProgress(30, 9, 32, 78.9);
        GameProgress[] gps = {gp1, gp2, gp3};

        String[] savesPaths = new String[gps.length];
        for (int i = 0; i < gps.length; i++) {
            String filePath = savePath + SEP + "save" + i + ".dat";
            savesPaths[i] = filePath;
            saveGame(filePath, gps[i]);
        }
        zipFiles(savePath + SEP + "zipped.zip", savesPaths);


    }

    private static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path, false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            System.out.println("Объект " + gameProgress + " сохранён");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void zipFiles(String path, String[] files) {
        File pathFile = new File(path);
        if (!pathFile.exists()) {
            try {
                pathFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileInputStream fis = new FileInputStream(path);
             ZipOutputStream zos = new ZipOutputStream(
                     new FileOutputStream(path))) {
            for (String filePath : files) {
                ZipEntry entry = new ZipEntry(new File(filePath).getName());
                zos.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zos.write(buffer);
                zos.closeEntry();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
