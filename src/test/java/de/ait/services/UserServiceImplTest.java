package de.ait.services;

import de.ait.model.User;
import de.ait.repositories.UserRepository;
import de.ait.repositories.UserRepositoryFileImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceImplTest {
    UserRepository repository;
    UserService service;

    final String EXISTS_USER_EMAIL= "jack3@mail.com";
    final String NOT_EXISTS_USER_EMAIL= "ann@mail.com";
    // Mockito
    @BeforeEach
    void setUp() {
        repository = Mockito.mock(UserRepository.class);
        service = new UserServiceImpl(repository);

        // когда в где-то в тесте будет вызван метод repository.findAll() система вернет именно этот лист
        Mockito.when(repository.findAll()).thenReturn(List.of(
           new User(1L,"jack1","jack1@mail.com"),
           new User(2L,"jack2","jack2@mail.com"),
           new User(3L,"jack3","jack3@mail.com"),
           new User(4L,"jack4","jack4@mail.com")
        ));

        Mockito.when(repository.findByEmail(EXISTS_USER_EMAIL)).thenReturn(new User(3L,"jack3", "jack3@mail.com"));
        Mockito.when(repository.findByEmail(NOT_EXISTS_USER_EMAIL)).thenReturn(null);



    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllUsers() {
        List<User> expected = List.of(
                new User(1L,"jack1","jack1@mail.com"),
                new User(2L,"jack2","jack2@mail.com"),
                new User(3L,"jack3","jack3@mail.com"),
                new User(4L,"jack4","jack4@mail.com")
        );

        List<User> actual = service.getAllUsers();

        Assertions.assertEquals(expected,actual);
    }

    @Test
    void checkIfUserExistTest(){
     Assertions.assertAll(
             ()->Assertions.assertTrue(service.checkIfUserExist(EXISTS_USER_EMAIL)),
             ()->Assertions.assertFalse(service.checkIfUserExist(NOT_EXISTS_USER_EMAIL))
     );

    }

    @Test
    void normal_create_user(){
        User user = new User("ann", NOT_EXISTS_USER_EMAIL);

        service.createUser("ann", NOT_EXISTS_USER_EMAIL);
        Mockito.verify(repository, Mockito.times(1)).save(user);
    }

    @Test
    void create_user_with_existing_email(){
        User user = new User("jack3", EXISTS_USER_EMAIL);

        Assertions.assertAll(
                // проверяем, что метод вернул RuntimeException
                ()-> assertThrows(RuntimeException.class, ()->service.createUser("jack3", EXISTS_USER_EMAIL)),
                // проверяем, что во время теста метод save(user) в репозитории не вызывался
                ()-> Mockito.verify(repository, Mockito.never()).save(user)

        );
    }

}