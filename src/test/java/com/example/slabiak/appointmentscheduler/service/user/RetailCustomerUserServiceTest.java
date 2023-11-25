package com.example.slabiak.appointmentscheduler.service.user;

import com.example.slabiak.appointmentscheduler.dao.RoleRepository;
import com.example.slabiak.appointmentscheduler.dao.user.customer.RetailCustomerRepository;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.customer.RetailCustomer;
import com.example.slabiak.appointmentscheduler.model.UserForm;
import com.example.slabiak.appointmentscheduler.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RetailCustomerUserServiceTest {

 @Mock
 private BCryptPasswordEncoder passwordEncoder;

 @Mock
 private RoleRepository roleRepository;

 @Mock
 private RetailCustomerRepository retailCustomerRepository;

 @InjectMocks
 private UserServiceImpl userService;

 UserForm retailUserForm;
 RetailCustomer retailCustomer;
 Optional<RetailCustomer> optionalRetailCustomer;

 private int userId;
 private String userName;
 private String password;
 private String matchingPassword;
 private String firstName;
 private String lastName;
 private String email;
 private String mobile;
 private String street;
 private String postcode;
 private String city;

 String passwordEncoded;

 private String roleNameCustomer;
 private String roleNameRetailCustomer;
 private Role roleCustomer;
 private Role roleRetailCustomer;
 Collection<Role> retailCustomerRoles;

 @Before
 public void initObjects() {
     initUserDetails();
     initPasswordEncoded();
     initRetailUserForm();
     initRoles();
 }

 private void initUserDetails() {
     userId = 1;
     userName = "username";
     firstName = "firstname";
     lastName = "lastname";
     email = "email@example.com";
     mobile = "123456789";
     street = "street";
     postcode = "12-345";
     city = "city";
 }

 private void initPasswordEncoded() {
     password = "password";
     matchingPassword = "password";
     passwordEncoded = "xxxx";
 }

 private void initRetailUserForm() {
     retailUserForm = new UserForm();
     retailUserForm.setUserName(userName);
     retailUserForm.setPassword(password);
     retailUserForm.setMatchingPassword(matchingPassword);
     retailUserForm.setFirstName(firstName);
     retailUserForm.setLastName(lastName);
     retailUserForm.setEmail(email);
     retailUserForm.setMobile(mobile);
     retailUserForm.setStreet(street);
     retailUserForm.setPostcode(postcode);
     retailUserForm.setCity(city);
     retailUserForm.setId(userId);
 }

 private void initRoles() {
     roleNameCustomer = "ROLE_CUSTOMER";
     roleNameRetailCustomer = "ROLE_CUSTOMER_RETAIL";
     roleCustomer = new Role(roleNameCustomer);
     roleRetailCustomer = new Role(roleNameRetailCustomer);
     retailCustomerRoles = new HashSet<>();
     retailCustomerRoles.add(roleCustomer);
     retailCustomerRoles.add(roleRetailCustomer);
 }

 @Test
 public void shouldSaveNewRetailCustomer() {
     ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
     userService.saveNewRetailCustomer(retailUserForm);

     verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
 }

 @Test
 public void shouldEncodePasswordWhenForNewRetailCustomer() {
     when(passwordEncoder.encode(password)).thenReturn(passwordEncoded);

     verifyAndCaptureRetailCustomer();
     assertEquals(retailCustomer.getPassword(), passwordEncoded);
 }

 @Test
 public void userFormDataShouldMatchRetailCustomerObject() {
     verifyAndCaptureRetailCustomer();
     assertRetailCustomerEqualsFormData(retailCustomer, retailUserForm);
 }

 @Test
 public void shouldAssignTwoRolesForRetailCustomer() {
     mockRoleRepository();
     verifyAndCaptureRetailCustomer();
     assertEquals(retailCustomer.getRoles().size(), 2);
 }

 @Test
 public void shouldAssignCorrectRolesForRetailCustomer() {
     mockRoleRepository();
     verifyAndCaptureRetailCustomer();
     assertTrue(retailCustomer.hasRole(roleNameRetailCustomer));
     assertTrue(retailCustomer.hasRole(roleNameCustomer));
 }

 @Test
 public void shouldUpdateRetailCustomerProfileData() {
     mockRetailCustomerToUpdate();
     verifyAndCaptureRetailCustomer();
     assertRetailCustomerEqualsFormData(retailCustomer, retailUserForm);
 }

 @Test
 public void shouldNotAffectUsernameAndPasswordAndRolesWhileRetailCustomerProfileUpdate() {
     mockRetailCustomerToUpdate();
     String currentUsername = "username2";
     String currentPassword = "password2";
     retailCustomer.setUserName(currentUsername);
     retailCustomer.setPassword(currentPassword);
     retailCustomer.setRoles(retailCustomerRoles);

     verifyAndCaptureRetailCustomer();

     assertEquals(retailCustomer.getUserName(), currentUsername);
     assertEquals(retailCustomer.getPassword(), currentPassword);
     assertEquals(retailCustomer.getRoles(), retailCustomerRoles);
 }

 @Test
 public void shouldFindRetailCustomerById() {
     mockRetailCustomerToFind();
     assertEquals(optionalRetailCustomer.get(), userService.getRetailCustomerById(userId));
     verify(retailCustomerRepository, times(1)).findById(userId);
 }

 private void verifyAndCaptureRetailCustomer() {
     ArgumentCaptor<RetailCustomer> argumentCaptor = ArgumentCaptor.forClass(RetailCustomer.class);
     userService.saveNewRetailCustomer(retailUserForm);
     verify(retailCustomerRepository, times(1)).save(argumentCaptor.capture());
     retailCustomer = argumentCaptor.getValue();
 }

 private void mockRoleRepository() {
     doReturn(roleRetailCustomer).when(roleRepository).findByName(roleNameRetailCustomer);
     doReturn(roleCustomer).when(roleRepository).findByName(roleNameCustomer);
 }

 private void mockRetailCustomerToUpdate() {
     RetailCustomer customerToBeUpdated = new RetailCustomer();
     customerToBeUpdated.setId(userId);
     doReturn(customerToBeUpdated).when(retailCustomerRepository).getOne(userId);
 }

 private void mockRetailCustomerToFind() {
     retailCustomer = new RetailCustomer();
     retailCustomer.setId(userId);
     retailCustomer.setUserName(userName);
     optionalRetailCustomer = Optional.of(retailCustomer);
     when(retailCustomerRepository.findById(userId)).thenReturn(optionalRetailCustomer);
 }

 private void assertRetailCustomerEqualsFormData(RetailCustomer retailCustomer, UserForm retailUserForm) {
     assertEquals(retailCustomer.getUserName(), retailUserForm.getUserName());
     assertEquals(retailCustomer.getFirstName(), retailUserForm.getFirstName());
     assertEquals(retailCustomer.getLastName(), retailUserForm.getLastName());
     assertEquals(retailCustomer.getEmail(), retailUserForm.getEmail());
     assertEquals(retailCustomer.getMobile(), retailUserForm.getMobile());
     assertEquals(retailCustomer.getStreet(), retailUserForm.getStreet());
     assertEquals(retailCustomer.getCity(), retailUserForm.getCity());
     assertEquals(retailCustomer.getPostcode(), retailUserForm.getPostcode());
 }
}
