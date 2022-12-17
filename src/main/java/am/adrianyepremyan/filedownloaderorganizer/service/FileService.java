package am.adrianyepremyan.filedownloaderorganizer.service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    private final String rootPath;

    FileService(@Value("${rootPath}") String rootPath) {
        this.rootPath = rootPath;
    }

    public File createFolder(String dir) {
        final var folder = new File(rootPath + "/" + dir);
        folder.mkdirs();
        return folder;
    }

    @SneakyThrows
    public File createFile(File folder, String filename, String ext) {
        final var file = new File(folder.getAbsolutePath() + "/" + filename + ext);
        file.createNewFile();
        return file;
    }

    @SneakyThrows
    public void writeIntoFile(String dir, String filename, String ext, byte[] bytes) {
        final var folder = createFolder(dir);
        final var file = createFile(folder, filename, ext);
        final var fos = new FileOutputStream(file);
        System.out.println("Writing bytes into the file...");
        fos.write(bytes);
    }

    @SneakyThrows
    public Stream<String> getFilesUnderDirStream(String dir, int maxDepth) {
        final var dirPath = Path.of(dir);
        if (!Files.exists(dirPath)) {
            return Stream.empty();
        }

        return Files.walk(dirPath, maxDepth)
            .map(Path::toFile)
            .filter(File::isFile)
            .map(File::getName);
    }
}
