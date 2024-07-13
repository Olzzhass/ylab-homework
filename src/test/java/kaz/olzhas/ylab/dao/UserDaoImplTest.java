//package kaz.olzhas.dao;
//
//import kaz.olzhas.containers.PostgresTestContainer;
//import kaz.olzhas.ylab.dao.implementations.UserDaoImpl;
//import kaz.olzhas.ylab.entity.User;
//import kaz.olzhas.ylab.liquibase.LiquibaseDemo;
//import kaz.olzhas.ylab.util.ConnectionManager;
//import org.junit.jupiter.api.*;
//import org.assertj.core.api.Assertions;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@DisplayName("user dao implementation test")
//public class UserDaoImplTest extends PostgresTestContainer {
//
//    private UserDaoImpl userDao;
//
//    private ConnectionManager connectionManager = new ConnectionManager(
//            container.getJdbcUrl(), container.getUsername(), container.getPassword(),
//            "org.postgresql.Driver");
//
//    @BeforeEach
//    public void setUp() {
//
//        LiquibaseDemo liquibaseTest = new LiquibaseDemo(connectionManager.getConnection(), "db/main-changelog.xml", "liquibase");
//        liquibaseTest.runMigrations();
//
//        userDao = new UserDaoImpl(connectionManager);
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("find by username method verification test")
//    public void findByUsernameTest(){
//        User user = new User();
//        user.setUsername("Test");
//        user.setPassword("test");
//
//        userDao.save(user);
//
//        Optional<User> maybeUser = userDao.findByUsername(user.getUsername());
//
//        assertAll(
//                () -> assertThat(maybeUser).isPresent(),
//                () -> assertThat(maybeUser.get().getUsername()).isEqualTo(user.getUsername()),
//                () -> assertThat(maybeUser.get().getPassword()).isEqualTo(user.getPassword())
//        );
//    }
//
//    @Test
//    @Order(1)
//    @DisplayName("User find all method verification test")
//    public void findAllTest(){
//        User user = new User();
//        user.setUsername("Test");
//        user.setPassword("test");
//
//        User user2 = new User();
//        user2.setUsername("Test2");
//        user2.setPassword("test2");
//
//        userDao.save(user);
//        userDao.save(user2);
//
//        List<User> allUsers = userDao.findAll();
//        assertAll(
//                () -> assertFalse(allUsers.isEmpty()),
//                () -> assertThat(allUsers).hasSize(3) //+ пользователь который добавляется при миграции
//        );
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("User save method verification test")
//    public void saveTest(){
//        User user = new User();
//        user.setUsername("Test");
//        user.setPassword("test");
//
//        boolean is = userDao.save(user);
//        Optional<User> inserted = userDao.findByUsername(user.getUsername());
//
//        assertAll(
//                () -> assertThat(is).isEqualTo(true),
//                () -> assertThat(inserted).isPresent(),
//                () -> assertThat(inserted.get().getUsername()).isEqualTo(user.getUsername())
//        );
//    }
//
//    @Test
//    @Order(4)
//    @DisplayName("find by id method verification test")
//    public void getByIdTest(){
//        User testUser = new User();
//        testUser.setUsername("Test");
//        testUser.setPassword("test");
//        userDao.save(testUser);
//
//        User user = userDao.getById(2L);
//
//        assertAll(
//                () -> assertThat(user.getUsername()).isEqualTo(testUser.getUsername()),
//                () -> assertThat(user.getPassword()).isEqualTo(testUser.getPassword())
//        );
//    }
//}
