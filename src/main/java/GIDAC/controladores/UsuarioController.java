package GIDAC.controladores;

import GIDAC.modelo.CorreoElectronico;
import GIDAC.modelo.GrupoInvestigacion;
import GIDAC.modelo.Usuario;
import GIDAC.servicios.GrupoInvestigacionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.multipart.MultipartFile;
import GIDAC.servicios.UsuarioService;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin("*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    
    @Autowired
    private GrupoInvestigacionService grupoInvestigacionService;
    
    
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private JavaMailSender mailSender;
   

    @PostMapping("/")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) throws Exception{
        
        cValidaciones validaciones=new cValidaciones();
        String email=usuario.getEmail();
        String clave=generarClave();
        usuario.setFechaCreacion(validaciones.fechaActual());
        usuario.setContrasenia(this.bCryptPasswordEncoder.encode(clave));

        System.out.println("-----------------------------------------------------------");
        System.out.println("Contraseña: "+clave);

        return usuarioService.guardarUsuario(usuario);
        
    }
//    public ResponseEntity<?> guardarUsuario(@RequestBody Usuario usuario) throws Exception{
//        try{
//            String email=usuario.getEmail();
//            String clave=generarClave();
//            usuario.setContrasenia(this.bCryptPasswordEncoder.encode(clave));
//            if(enviarCorreoElectronico1(email,clave)==true){
//                usuarioService.guardarUsuario(usuario);
//                return ResponseEntity.ok("Usuario guardado exitosamente");
//            }else{
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
//            }
//        }catch(Exception e){
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
//        }
//    }
    
    public boolean enviarCorreoElectronico1(Usuario usuario, String clave) {
        
            CorreoElectronico correoElectronico=new CorreoElectronico();
            cValidaciones validaciones =new cValidaciones();
            String mensaje=correoElectronico.registrarUsuarioMensaje(usuario.getNombreUsuario(), usuario.getApellidoUsuario(), "cedula", usuario.getEmail(), clave, validaciones.fechaActual());
            
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(usuario.getEmail());
            message.setFrom("espoch.gidac@outlook.com");
            message.setSubject("Registro de administrador");
            message.setText(mensaje);
            
            try {
                mailSender.send(message);
                return true;
            } catch (MailException e) {
                System.out.println("error "+e);
                return false;
            }
    }
    
    private static final String CARACTERES =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public String generarClave() {
        StringBuilder sb = new StringBuilder(10);
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 10; i++) {
            sb.append(CARACTERES.charAt(random.nextInt(CARACTERES.length())));
        }
        return sb.toString();
    }
    
    @GetMapping("/usuario-rol/{idRol}")
    public List<Usuario> obtenerUsuarioPorRol(@PathVariable("idRol") Integer idRol){
        return usuarioService.usuarioPorRol(idRol);
    }
    
    @GetMapping("/usuario-rol/eliminado/{idRol}")
    public List<Usuario> obtenerUsuarioPorRolEliminado(@PathVariable("idRol") Integer idRol){
        return usuarioService.usuarioPorRolEliminado(idRol);
    }
    
    @GetMapping("/restaurar-usuario/{idUsuario}")
    public void restaurarUsuario(@PathVariable("idUsuario") Integer idUsuario){
        cValidaciones validaciones=new cValidaciones();
        Usuario usuario= usuarioService.obtenerUsuarioId(idUsuario);
        usuario.setFechaActualizacion(validaciones.fechaActual());
        usuario.setVigencia(true);
        usuarioService.actualizarUsuario(usuario);
    }
    
    @GetMapping("/{email}")
    public Usuario obtenerUsuario(@PathVariable("email") String email){
        return usuarioService.obtenerEmail(email);
    }

    @DeleteMapping("/{idUsuario}")
    public void eliminarUsuario(@PathVariable("idUsuario") Integer idUsuario) throws Exception{
        cValidaciones validaciones=new cValidaciones();
        Usuario usuario=usuarioService.obtenerUsuarioId(idUsuario);
        usuario.setVigencia(false);
        usuario.setFechaActualizacion(validaciones.fechaActual());
        if(usuario.getRol().getIdRol()!=3){
          if(null!=grupoInvestigacionService.obtenerInvestigacionesDeInvestigadorVigentes(idUsuario)){
            List<GrupoInvestigacion> listaGrupo=grupoInvestigacionService.obtenerInvestigacionesDeInvestigadorVigentes(idUsuario);
            for (GrupoInvestigacion grupo : listaGrupo) {
                grupo.setFechaActualizacion(validaciones.fechaActual());
                grupo.setVigencia(false);
                grupoInvestigacionService.save(grupo);
            }
         } 
        }
        usuarioService.actualizarUsuario(usuario);
    }
    
    @DeleteMapping("/eliminar-investigador/{idUsuario}/{idProyectoInvestigacion}")
    public void eliminarUsuarioInvestigador(@PathVariable("idUsuario") Integer idUsuario, @PathVariable("idProyectoInvestigacion") Integer idProyectoInvestigacion) throws Exception{
        cValidaciones validaciones=new cValidaciones();
        Usuario usuario=usuarioService.obtenerUsuarioId(idUsuario);
        usuario.setVigencia(false);
        usuario.setFechaActualizacion(validaciones.fechaActual());
        if(usuario.getRol().getIdRol()==2){
         if(null!=grupoInvestigacionService.obtenerInvestigacionesDeInvestigadorVigentes(idUsuario)){
            GrupoInvestigacion directorAreaInvestigacion=(GrupoInvestigacion) grupoInvestigacionService.obtenerInvestigacionesDeInvestigadorVigentes(idUsuario);
            directorAreaInvestigacion.setFechaActualizacion(validaciones.fechaActual());
            directorAreaInvestigacion.setVigencia(false);
            grupoInvestigacionService.save(directorAreaInvestigacion);
         }
        }
        usuarioService.actualizarUsuario(usuario);
    }
    
    
    @GetMapping("/id-usuario/{idUsuario}")
    public Usuario obtenerUsuario(@PathVariable("idUsuario") Integer idUsuario){
        return usuarioService.obtenerUsuarioId(idUsuario);
    }

    @GetMapping("/lista-normal")
    public List<Usuario>listarUsuarios(){
        return usuarioService.obtenerNormal();
    }    
    
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Integer id) {
      Usuario usuario = usuarioService.obtenerUsuarioId(id);
      if (usuario != null) {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(usuario.getImagenPerfil());
      } else {
        return ResponseEntity.notFound().build();
      }
    }
    
    @GetMapping("/buscar-investigador-no-asignado/{id}")
    public List<Usuario> buscarInvestigadoresNoAsignados(@PathVariable Integer id)
    {
        return usuarioService.buscarInvestigadoresNoAsignados(id);
    }
    
    
    
    
    
    
    
    
    
    /*
    @PostMapping("/enviarCorreo/")
    public ResponseEntity<?> enviarCorreoElectronico(@RequestBody CorreoElectronico correoElectronico) {
        Usuario usuarioLocal = usuarioService.obtenerPorEmail(correoElectronico.getDestinatario());
        if(usuarioLocal != null){
            String clave=generarClave();
            String claveEnviar="La su nueva clave es: "+clave;
            usuarioLocal.setPassword(this.bCryptPasswordEncoder.encode(clave));
            usuarioService.actualizarUsuario(usuarioLocal);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(correoElectronico.getDestinatario());
            message.setSubject(correoElectronico.getAsunto());
            message.setText(claveEnviar);
            try {
                mailSender.send(message);
                return ResponseEntity.ok("Correo electrónico enviado correctamente.");
            } catch (MailException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo electrónico.");
            }
        }
        else{
            System.out.println("El usuario no existe");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("El correo no esta registrado en el sistema.");
        }
    }
    */
    

    
    /*
    @GetMapping("/")
    public ResponseEntity<?>listarUsuariosNormales(){
        
        List<Usuario> us = usuarioService.obtenerUsuarios();
        List<Usuario> usFinal=null;
        
        for(int i=0;i<us.size();i++){
            
            Usuario oU=us.get(i);
            System.out.println(oU.getUsuarioRoles().getClass());
            System.out.println("Lega-----------------------------------------------------");
            
            
        }
        return usFinal;
        return ResponseEntity.ok(usuarioService.obtenerUsuarios());
    }
*/
    
   
    
    /*
    @PutMapping("/")
    public Usuario editarUsuario(@RequestBody Usuario usuario, @RequestParam("file") MultipartFile file) throws Exception{
        
        usuario.setPerfil("default.png");

        usuario.setPassword(this.bCryptPasswordEncoder.encode(usuario.getPassword()));
        
        if(!file.isEmpty()){
            try{
                usuario.setDatos(file.getBytes());
            }catch(Exception e){
                System.out.println("Error al ingresar la imagen");
            }
        }
        
        return usuarioService.actualizarUsuario(usuario);
    }
*/

}