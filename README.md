Storage helper est une librairie Java permettant de gérer les processus
de téléchargement de fichier en gérant différentes extensions **(****.pdf,.jpg,.jpeg,
.png,.xlsx,.docx)**

Dépendance maven à ajouter sur le pom.xml:  

`<dependency>
  <groupId>io.github.misterseye</groupId>
  <artifactId>storage-helper</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>`


Pour utiliser la librairie, il faut ajouter le namespace sur la classe 
principale :


exemple:  

`@SpringBootApplication
@ComponentScan({"io.github.misterseye.*", "com.example.testupload.*"})
public class TestUploadApplication {
   public static void main(String[] args) {
    SpringApplication.run(TestUploadApplication.class, args);
}`

Seul bémol, il faut ajouter le scan du package principal de votre projet pour la visibilité.  

On fait appel au service FileStorageHandle contenant différentes méthodes de signature :  
.`public DocumentInfoResponse storeFile(String baseUrl,Optional<MultipartFile> file);` :  
cette méthode prend une **baseUrl** et un optional **file** pour vérifier la présence du fichier pour le process
le baseUrl est utilisé pour matcher un url d'accés de votre fichier au niveau de base de données.  
exemple de baseUrl: `http://localhost:8080`

.`public DocumentInfoResponse storeFile(MultipartFile file)`; :
Pour uploader un fichier donné sans spécifier une base Url d'acces  

.`public DocumentInfoResponse storeFile(Optional<MultipartFile> file)` :  
Pour uploader un fichier en optional vérifiant la présence  

. `public List<DocumentInfoResponse> storeAllFiles(MultipartFile[] files)`:  
Pour uploader un ensemble de fichier  

.`public List<DocumentInfoResponse> storeAllFiles(String baseUrl,MultipartFile[] files)` :
Pour uploader un ensemble de fichier avec le baseUrl d'accés au fichier depuis un endpoint api .  

Les formats pour les URL d'acces des fichiers que vous devez exposer comme endpoint  :

 `URI_JPEG ="/formatjpeg/";`    
 `URI_PNG = "/formatpng/";`   
 `URI_PDF =  "/formatpdf/";`  
 `URI_DOCX =  "/formatdocx/";`  
 `URI_EXCEL =  "/formatxlsx/";`

Exemple sur un endpoint :  

`@GetMapping(value="/formatjpeg/{fileName}",produces = MediaType.IMAGE_PNG_VALUE)
@ResponseBody
public FileSystemResource getimfileimageJpeg(@PathVariable("fileName") String fileName)  {
return new FileSystemResource(new File("./uploads/"+fileName));
}`

Lors de l'utilisation de la librairie , il ne faut oublier d'ajouter le chemin
de votre directory de stockage sur l'application properties. Il se fait comme suit :  

application.properties :
`file.upload-dir=storage`

exemple d'utilisation sur un projet spring : 

`@RestController  
public class TestController {
@Autowired
private FileStorageHandler fileStorageHandler;
`

`@PostMapping(value = "store",
consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE)
public DocumentInfoResponse upload(@RequestParam("file") MultipartFile file){
return fileStorageHandler.storeFile(file);
}`


Le format de réponse de **DocumentInfoResponse**:  

`private String fileName;`  
`private String contentType;`
`private String fileAccessUri;`
`private String typeDocument;`

**fileName :** nom du fichier  
**contentType :** type de format du contenu  
**fileAcessURI :** url d'acces du fichier depuis le serveur (en général à persister à la base)  
**typeDocument:** type de document (facultatif)
