package com.diplom.web_service_attendance.service.security;

//import com.diplom.web_service_attendance.repository.security.UserRepository;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import com.diplom.web_service_attendance.entity.securityEntity.Authority;
//
//@Service
//@RequiredArgsConstructor
//public class WebUserDetailsService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return userRepository.findByUsername(username).map(user -> User.builder()
//                        .username(user.getUsername())
//                        .password(user.getPassword())
//                        .authorities(user.getAuthorities().stream()
//                                .map(Authority::getAuthority)
//                                .map(SimpleGrantedAuthority::new)
//                                .toList())
//                        .build())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found: %s".formatted(username)));
//    }
//}
