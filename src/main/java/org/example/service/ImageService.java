package org.example.service;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Сервис для работы с изображениями, который позволяет загружать и получать изображения в MongoDB с использованием GridFS.
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    private final GridFsTemplate gridFsTemplate;

    /**
     * Загружает изображение в базу данных MongoDB с использованием GridFS.
     *
     * @param filePath Путь к файлу изображения, который необходимо загрузить.
     * @return Строка, представляющая уникальный идентификатор загруженного файла в формате Hex.
     * @throws IOException Если возникает ошибка при чтении файла.
     */
    public String uploadImage(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath)) {
            ObjectId fileId = gridFsTemplate.store(inputStream, filePath, "image/jpeg");
            return fileId.toString();
        }
    }

    /**
     * Метод для получения изображения из MongoDB по идентификатору.
     *
     * @param id идентификатор файла
     * @return ресурс GridFS
     */
    public GridFsResource getImage(String id) {
        return gridFsTemplate.getResource(id);
    }
}