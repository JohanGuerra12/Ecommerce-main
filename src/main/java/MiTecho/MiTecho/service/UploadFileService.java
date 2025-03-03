package MiTecho.MiTecho.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileService {

    private String folder = System.getProperty("user.dir") + "/uploads/";

    public String saveImage(MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            File dir = new File(folder);
            if (!dir.exists()) {
                dir.mkdirs(); // Crea la carpeta si no existe
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(folder + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        }
        return "default.jpg";
    }

    public void deleteImage(String nombre) {
        File file = new File(folder + nombre);
        if (file.exists()) {
            file.delete();
        }
    }
}
