package kaz.olzhas.dao;

import kaz.olzhas.containers.PostgresTestContainer;
import kaz.olzhas.ylab.dao.WorkspaceDao;
import kaz.olzhas.ylab.dao.implementations.UserDaoImpl;
import kaz.olzhas.ylab.dao.implementations.WorkspaceDaoImpl;
import kaz.olzhas.ylab.entity.Workspace;
import kaz.olzhas.ylab.liquibase.LiquibaseDemo;
import kaz.olzhas.ylab.util.ConnectionManager;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Workspace dao implementation test")
public class WorkspaceDaoImplTest extends PostgresTestContainer {

    private WorkspaceDao workspaceDao;

    private ConnectionManager connectionManager = new ConnectionManager(
            container.getJdbcUrl(), container.getUsername(), container.getPassword(),
            "org.postgresql.Driver");

    @BeforeEach
    public void setUp() {

        LiquibaseDemo liquibaseTest = new LiquibaseDemo(connectionManager.getConnection(), "db/main-changelog.xml", "liquibase");
        liquibaseTest.runMigrations();

        workspaceDao = new WorkspaceDaoImpl(connectionManager);
    }

    @Test
    @Order(1)
    @DisplayName("Workspace find all method verification test")
    public void findAllTest(){
        Workspace workspace = new Workspace();
        workspace.setName("Workspace-1");

        Workspace workspace1 = new Workspace();
        workspace1.setName("Workspace-2");

        boolean is = workspaceDao.save(workspace);
        boolean is1 = workspaceDao.save(workspace1);

        List<Workspace> workspaceList = workspaceDao.findAll();

        assertAll(
                () -> assertThat(is).isEqualTo(true),
                () -> assertThat(is1).isEqualTo(true),
                () -> assertFalse(workspaceList.isEmpty()),
                () -> assertThat(workspaceList).hasSize(2)
        );
    }

    @Test
    @Order(2)
    @DisplayName("Workspace find by id method verification test")
    public void findByIdTest(){
        Workspace workspace = new Workspace();
        workspace.setName("Workspace-Test");

        boolean is = workspaceDao.save(workspace);

        Optional<Workspace> maybeWorkspace = workspaceDao.findById(3L); //Предыдущие тесты save()

        assertAll(
                () -> assertThat(is).isEqualTo(true),
                () -> assertThat(maybeWorkspace).isPresent(),
                () -> assertThat(maybeWorkspace.get().getName()).isEqualTo(workspace.getName())
        );
    }

    @Test
    @DisplayName("Workspace save method verification test")
    public void saveTest(){
        Workspace workspace = new Workspace();
        workspace.setName("Workspace-3");

        boolean is = workspaceDao.save(workspace);

        Optional<Workspace> maybeWorkspace = workspaceDao.findById(4L);

        assertAll(
                () -> assertThat(is).isEqualTo(true),
                () -> assertThat(maybeWorkspace).isPresent(),
                () -> assertThat(maybeWorkspace.get().getName()).isEqualTo(workspace.getName())
        );
    }
}
