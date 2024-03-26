package io.github.misterseye.service;

import io.github.misterseye.exceptions.FileStorageException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import io.github.misterseye.dto.DocumentInfoResponse;
import io.github.misterseye.properties.FileProperties;
import io.github.misterseye.validator.ValidatorExtension;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Component
public class FileStorageHandler {
    private final Path fileStorageLocation;

   private static final Logger LOGGER= LoggerFactory.getLogger(FileStorageHandler.class);
    public static final String URI_JPEG ="/formatjpeg/";
    public static final String URI_PNG = "/formatpng/";
    public static final String URI_PDF =  "/formatpdf/";
    public static final String URI_DOCX =  "/formatdocx/";
    public static final String URI_EXCEL =  "/formatxlsx/";


  public FileStorageHandler(FileProperties fileProperties){
       this.fileStorageLocation = Paths.get(fileProperties.getUploadDir())
             .toAbsolutePath().normalize();
   }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

   public DocumentInfoResponse storeFile(String baseUrl,Optional<MultipartFile> file){
       LOGGER.info("Process to store file !!");
       if(file.isPresent()){
           MultipartFile fileUploaded = file.get();
           String fileName = StringUtils.cleanPath(fileUploaded.getOriginalFilename());
           if (!ValidatorExtension.validateExtension(fileName)) {
               LOGGER.error("extension du fichier n'est pas accepté !!");
               throw new FileStorageException("l'extension du fichier n'est pas accepté");
           }
           try {
               if(fileName.contains("..")) {
                   LOGGER.error("fileName is invalid path sequence");
                   throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
               }
               Path targetLocation = this.fileStorageLocation.resolve(fileName);
               Files.copy(fileUploaded.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
               String documentUri;
              documentUri =setUrlAccessServer(baseUrl,fileName);
              DocumentInfoResponse documentInfoResponse = new DocumentInfoResponse();
              documentInfoResponse.setFileAccessUri(documentUri);
              documentInfoResponse.setContentType(fileUploaded.getContentType());
              documentInfoResponse.setFileName(fileName);
              documentInfoResponse.setTypeDocument("");
               LOGGER.info("SuccessFully upload file !!! ");
               return documentInfoResponse;
           }catch (IOException ex) {
               LOGGER.error("Error to store file !!");
               throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
           }
       }else{
           LOGGER.info("Document not present");
           throw new FileStorageException("Document not exist");
       }
   }


   public DocumentInfoResponse storeFile(MultipartFile file){
       LOGGER.info("Process to store file !!");
       String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       if (!ValidatorExtension.validateExtension(fileName)) {
           LOGGER.error("extension du fichier n'est pas accepté !!");
           throw new FileStorageException("l'extension du fichier n'est pas accepté");
       }
       try {
           if(fileName.contains("..")) {
               LOGGER.error("fileName is invalid path sequence");
               throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
           }
           Path targetLocation = this.fileStorageLocation.resolve(fileName);
           Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
           DocumentInfoResponse documentInfoResponse = new DocumentInfoResponse();
           documentInfoResponse.setFileAccessUri("");
           documentInfoResponse.setContentType(file.getContentType());
           documentInfoResponse.setFileName(fileName);
           documentInfoResponse.setTypeDocument("");
           LOGGER.info("SuccessFully upload file !!! ");
           return documentInfoResponse;
       }catch (IOException ex) {
           LOGGER.error("Error to store file !!");
           throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
       }
   }


    public DocumentInfoResponse storeFile(Optional<MultipartFile> file){
        LOGGER.info("Process to store file !!");
       if(file.isPresent()){
           MultipartFile fileUpload = file.get();
           String fileName = StringUtils.cleanPath(fileUpload.getOriginalFilename());
           if (!ValidatorExtension.validateExtension(fileName)) {
               LOGGER.error("extension du fichier n'est pas accepté !!");
               throw new FileStorageException("l'extension du fichier n'est pas accepté");
           }
           try {
               if(fileName.contains("..")) {
                   LOGGER.error("fileName is invalid path sequence");
                   throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
               }
               Path targetLocation = this.fileStorageLocation.resolve(fileName);
               Files.copy(fileUpload.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
               DocumentInfoResponse documentInfoResponse = new DocumentInfoResponse();
               documentInfoResponse.setFileAccessUri("");
               documentInfoResponse.setContentType(fileUpload.getContentType());
               documentInfoResponse.setFileName(fileName);
               documentInfoResponse.setTypeDocument("");
               LOGGER.info("SuccessFully upload file !!! ");
               return documentInfoResponse;
           }catch (IOException ex) {
               LOGGER.error("Error to store file !!");
               throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
           }
       }
       return null;
    }


   public List<DocumentInfoResponse> storeAllFiles(MultipartFile[] files){
       List<DocumentInfoResponse> infoResponses = new ArrayList<>();
    LOGGER.info("process store all file ");
       Arrays.stream(files).forEach(file -> {
           String fileName = StringUtils.cleanPath(file.getOriginalFilename());
           if (!ValidatorExtension.validateExtension(fileName)) {
               LOGGER.error("extension du fichier n'est pas accepté !!");
               throw new FileStorageException("l'extension du fichier n'est pas accepté");
           }
           try {
               if(fileName.contains("..")) {
                   LOGGER.error("fileName is invalid path sequence");
                   throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
               }
               Path targetLocation = this.fileStorageLocation.resolve(fileName);
               Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
               DocumentInfoResponse documentInfoResponse = new DocumentInfoResponse();
               documentInfoResponse.setFileAccessUri("");
               documentInfoResponse.setContentType(file.getContentType());
               documentInfoResponse.setFileName(fileName);
               documentInfoResponse.setTypeDocument("");
               infoResponses.add(documentInfoResponse);
           }catch (IOException ex) {
               LOGGER.error("Error to store file !!");
               throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
           }
       });
       LOGGER.info("SuccessFully upload all files !!! ");
       return infoResponses;
   }


    public List<DocumentInfoResponse> storeAllFiles(String baseUrl,MultipartFile[] files){
        List<DocumentInfoResponse> infoResponses = new ArrayList<>();
        LOGGER.info("process store all file ");
        Arrays.stream(files).forEach(file -> {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            if (!ValidatorExtension.validateExtension(fileName)) {
                LOGGER.error("extension du fichier n'est pas accepté !!");
                throw new FileStorageException("l'extension du fichier n'est pas accepté");
            }
            try {
                if(fileName.contains("..")) {
                    LOGGER.error("fileName is invalid path sequence");
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                DocumentInfoResponse documentInfoResponse = new DocumentInfoResponse();
                String documentUri =setUrlAccessServer(baseUrl,fileName);
                documentInfoResponse.setFileAccessUri(documentUri);
                documentInfoResponse.setContentType(file.getContentType());
                documentInfoResponse.setFileName(fileName);
                documentInfoResponse.setTypeDocument("");
                infoResponses.add(documentInfoResponse);
            }catch (IOException ex) {
                LOGGER.error("Error to store file !!");
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        });
        LOGGER.info("SuccessFully upload all files !!! ");
        return infoResponses;
    }


    private String  setUrlAccessServer(String baseUrl, String fileName) {
        String extension = getExtension(fileName);
        switch (extension) {
            case ".jpeg", ".jpg" -> {
                return getUri(baseUrl, fileName,URI_JPEG);
            }
            case ".png" -> {
                return getUri(baseUrl,fileName,URI_PNG);
            }
            case ".pdf" -> {
                return getUri(baseUrl,fileName,URI_PDF);
            }
            case ".docx" -> {
                return getUri(baseUrl,fileName,URI_DOCX);
            }
            case ".xlxs" -> {
                return getUri(baseUrl,fileName,URI_EXCEL);
            }
            default -> {
                LOGGER.error("not matching extension !!");
                return null;
            }
        }
    }

    private static String getUri(String baseUrl, String fileName,String formatUri) {
        String documentUri;
        documentUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(baseUrl + formatUri)
                .path(fileName)
                .toUriString();
        return documentUri;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

}
