import java.io.*;
import java.time.LocalDateTime;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {
    private static final String SEP = File.separator;

    public static void main(String[] args) {
        System.out.println("Домашнее задание к занятию 1.3\n" +
                "Потоки ввода-вывода. Работа с файлами. Сериализация\n" +
                "Задача 2: Сохранение");

        String savePath = "T:" + SEP + "Program Files" + SEP + "Games" + SEP + "savegames";
        String logPath = "T:" + SEP + "Program Files" + SEP + "Games" + SEP + "temp" + SEP + "temp.txt";
        GameProgress gp1 = new GameProgress(100, 3, 6, 23.1);
        GameProgress gp2 = new GameProgress(70, 5, 11, 47.3);
        GameProgress gp3 = new GameProgress(30, 9, 32, 78.9);
        GameProgress[] gps = {gp1, gp2, gp3};

        String[] savesPaths = new String[gps.length];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gps.length; i++) {
            String filePath = savePath + SEP + "save" + i + ".dat";
            savesPaths[i] = filePath;
            sb.append(saveGame(filePath, gps[i])).append("\r\n");
        }
        sb.append(zipFiles(savePath + SEP + "zipped.zip", savesPaths));
        sb.append(deleteFiles(savesPaths));
        log(sb.toString(), logPath);
    }

    private static String saveGame(String path, GameProgress gameProgress) {
        String result;
        LocalDateTime ldt;
        try (FileOutputStream fos = new FileOutputStream(path, false);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameProgress);
            ldt = LocalDateTime.now();
            result = "Объект " + gameProgress + " сохранён";
        } catch (IOException e) {
            ldt = LocalDateTime.now();
            result = "Объект " + gameProgress + " не удалось сохранить";
            System.out.println(e.getMessage());
        }
        System.out.println(result);
        return ldt + "\t" + result;
    }

    private static String zipFiles(String path, String[] files) {
        File pathZip = new File(path);
        StringBuilder sb = new StringBuilder();
        String temp;
        LocalDateTime ldt;
        if (!pathZip.exists()) {
            try {
                pathZip.createNewFile();
                ldt = LocalDateTime.now();
                temp = "Архив " + pathZip.getName() + " создан";
                System.out.println(temp);
                sb
                        .append(ldt)
                        .append("\t")
                        .append(temp)
                        .append("\r\n");
            } catch (IOException e) {
                e.printStackTrace();
                ldt = LocalDateTime.now();
                temp = "Не удалось создать архив сохранений";
                System.out.println(temp);
                sb
                        .append(ldt)
                        .append("\t")
                        .append(temp)
                        .append("\r\n");
            }
        }
        if (pathZip.exists()) {
            try (FileInputStream fis = new FileInputStream(path);
            ZipOutputStream zos = new ZipOutputStream(
                    new FileOutputStream(path))) {
                for (String filePath : files) {
                    File file = new File(filePath);
                    ZipEntry entry = new ZipEntry(file.getName());
                    zos.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zos.write(buffer);
                    zos.closeEntry();
                    ldt = LocalDateTime.now();
                    temp = "Файл " + file.getName() + " заархивирован";
                    System.out.println(temp);
                    sb
                            .append(ldt)
                            .append("\t")
                            .append(temp)
                            .append("\r\n");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                ldt = LocalDateTime.now();
                temp = "Архивация сохранений не удалась";
                System.err.println(temp);
                sb
                        .append(ldt)
                        .append("\t")
                        .append(temp)
                        .append("\r\n");
            }
        }
        return sb.toString();
    }

    private static String deleteFiles(String[] paths) {
        StringBuilder sb = new StringBuilder();
        String temp;
        LocalDateTime ldt;
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                if (file.delete()) {
                    temp = "Файл " + file.getName() + " удалён";
                } else {
                    temp = "Файл " + file.getName() + " не удалось удалить";
                }
                ldt = LocalDateTime.now();
                System.out.println(temp);
                sb
                        .append(ldt)
                        .append("\t")
                        .append(temp)
                        .append("\r\n");
            } else {
                ldt = LocalDateTime.now();
                temp = "Файл " + file.getName() + " не найден";
                System.out.println(temp);

                sb
                        .append(ldt)
                        .append("\t")
                        .append(temp)
                        .append("\r\n");
            }
        }
        return sb.toString();
    }

    private static void log(String data, String path) {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(data);
            System.out.println("Лог обновлен");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
