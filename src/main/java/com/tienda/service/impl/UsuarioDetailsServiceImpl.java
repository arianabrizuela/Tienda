package com.tienda.service.impl;

import com.tienda.dao.UsuarioDao;
import com.tienda.domain.Rol;
import com.tienda.domain.Usuario;
import com.tienda.service.UsuarioDetailsService;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsServiceImpl implements UsuarioDetailsService, UserDetailsService {
    
    @Autowired
    private UsuarioDao usuarioDao;
    
    @Autowired
    private HttpSession session;
    
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        
        //Se busca el usuario que pasamos por parametro en la base de datos
        Usuario usuario = usuarioDao.findByUsername(username);
        
        //Se valida si se recupero un usuario, sino lanza un error
        if(usuario==null){
            throw new UsernameNotFoundException(username);
        }
        
        //se recupero la informacion del usuario y se agrega la imagen del usuario
        session.removeAttribute("usuarioImagen");
        session.removeAttribute("nombreCompleto");
        session.setAttribute("usuarioImagen", usuario.getRutaImagen());
        session.setAttribute("nombreCompleto", usuario.getNombre() + " " + usuario.getApellidos());
        
        //Se van a recuperar los roles del usuario y se crean los roles ya como seguridad de Spring
        // BD: ADMIN ....ROLE_ADMIN
        var roles = new ArrayList<GrantedAuthority>();
        for (Rol rol : usuario.getRoles()){
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
        }
        
        return new User(usuario.getUsername(),usuario.getPassword(),roles);
    }
    
}
