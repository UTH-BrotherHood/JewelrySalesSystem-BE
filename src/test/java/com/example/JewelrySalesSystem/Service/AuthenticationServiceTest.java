package com.example.JewelrySalesSystem.Service;

import com.example.JewelrySalesSystem.dto.request.AuthenticationRequest;
import com.example.JewelrySalesSystem.dto.request.IntrospectRequest;
import com.example.JewelrySalesSystem.dto.response.AuthenticationResponse;
import com.example.JewelrySalesSystem.dto.response.IntrospectResponse;
import com.example.JewelrySalesSystem.entity.Employee;
import com.example.JewelrySalesSystem.entity.InvalidatedToken;
import com.example.JewelrySalesSystem.exception.AppException;
import com.example.JewelrySalesSystem.exception.ErrorCode;
import com.example.JewelrySalesSystem.repository.EmployeeRepository;
import com.example.JewelrySalesSystem.repository.InvalidatedTokenRepository;
import com.example.JewelrySalesSystem.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private InvalidatedTokenRepository invalidatedTokenRepository;

  @InjectMocks
  private AuthenticationService authenticationService;

  private final String SIGNER_KEY = "this_is_a_very_secure_key_256_bits_long"; // Đảm bảo chiều dài đủ

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    authenticationService.SIGNER_KEY = SIGNER_KEY;
  }

  @Test
  public void testAuthenticateSuccess() {
    Employee employee = new Employee();
    employee.setUsername("user");
    employee.setPassword(new BCryptPasswordEncoder().encode("password")); // Sử dụng mã hóa BCrypt

    when(employeeRepository.findByUsername("user")).thenReturn(Optional.of(employee));

    AuthenticationRequest request = new AuthenticationRequest("user", "password");
    AuthenticationResponse response = authenticationService.authenticate(request);

    assertTrue(response.isAuthenticated());
    assertNotNull(response.getToken());
  }
  @Test
  public void testAuthenticateFailure() {
    when(employeeRepository.findByUsername("user")).thenReturn(Optional.empty());

    AuthenticationRequest request = new AuthenticationRequest("user", "password");

    assertThrows(AppException.class, () -> authenticationService.authenticate(request));
  }

  @Test
  public void testIntrospectValidToken() throws JOSEException, ParseException {
    String token = generateValidToken();
    IntrospectRequest request = new IntrospectRequest(token);

    IntrospectResponse response = authenticationService.introspect(request);

    assertTrue(response.isValid());
  }

  @Test
  public void testIntrospectInvalidToken() throws JOSEException, ParseException {
    // Tạo token hợp lệ và sau đó thay đổi nó để trở thành không hợp lệ
    String validToken = generateValidToken();
    String invalidToken = validToken + "invalid"; // Thêm một chuỗi không hợp lệ vào token

    IntrospectRequest request = new IntrospectRequest(invalidToken);

    IntrospectResponse response = authenticationService.introspect(request);

    assertFalse(response.isValid()); // Đảm bảo rằng token không hợp lệ sẽ không được xác thực
  }


  private String generateValidToken() throws JOSEException {
    Employee employee = new Employee();
    employee.setUsername("user");
    employee.setEmployeeId(1L);

    // Giả lập ký và trả về token hợp lệ
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

    JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
            .subject(employee.getUsername())
            .issuer("jewelryStore")
            .expirationTime(new Date(
                    Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
            ))
            .claim("employeeID", employee.getEmployeeId())
            .jwtID(UUID.randomUUID().toString())
            .build();

    Payload payload = new Payload(jwtClaimsSet.toJSONObject());

    JWSObject jwsObject = new JWSObject(header, payload);
    jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
    return jwsObject.serialize();
  }


  private SignedJWT generateSignedJWT() throws JOSEException {
    // Tạo một SignedJWT giả lập để sử dụng trong test
    JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
            .subject("user")
            .issuer("issuer")
            .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
            .jwtID(UUID.randomUUID().toString())
            .build();

    SignedJWT signedJWT = new SignedJWT(
            new JWSHeader(JWSAlgorithm.HS256),
            claimsSet
    );
    signedJWT.sign(new MACSigner(SIGNER_KEY.getBytes()));
    return signedJWT;
  }
}
