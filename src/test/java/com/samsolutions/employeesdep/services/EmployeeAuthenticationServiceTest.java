package com.samsolutions.employeesdep.services;

//@SpringBootTest
public class EmployeeAuthenticationServiceTest {
    /*@Value("${spring.flyway.placeholders.admin_login}")
    private String adminLogin;
    @Value("${spring.flyway.placeholders.default_department}")
    private String defaultDepartment;

    @Autowired
    private EmployeeAuthenticationService empAuthenticationService;

    @Autowired
    private EmployeeService empService;

    @Autowired
    private DepartmentRepository departRepository;

    @Autowired
    private JpaRoleDao roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyPasswordEncoder encoder;

    private final List<Long> listIDs = new ArrayList<>();

    @BeforeEach
    public void setup() {
        EmployeeDTO empToSave, savedEmp;
        UserDTO userToSave;
        DepartmentDTO depart = new DepartmentDTO("Test Department");
        RoleDTO[] allRoles = {new RoleDTO("user"), new RoleDTO("admin"),new RoleDTO( "Tester")};

        // creating and saving employee 1
        Set<RoleDTO> roles1 = Set.of(allRoles[0], allRoles[1], allRoles[2]); // all roles
        empToSave = new EmployeeDTO("Krosh", "Smesharik", Gender.MALE,
                LocalDate.of(2010, 10, 11),
                LocalDate.of(2020, 7, 20));
        userToSave = new UserDTO("krosh", "kroshpwd", "krosh@gmail.com");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart);
        empToSave.setEmployeeRoles(roles1);
        savedEmp = empService.createEmployee(empToSave, false);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            depart = savedEmp.getDepartment();
            updateRoles(allRoles, savedEmp.getEmployeeRoles());
        }

        // creating and saving employee 2
        Set<RoleDTO> roles2 = Set.of(allRoles[0]); // User role
        empToSave = new EmployeeDTO("Nyusha", "Smesharik", Gender.FEMALE,
                LocalDate.of(2010, 1, 13), null);
        userToSave = new UserDTO("nyusha", "nyushapwd", "nyusha@gmail.com");
        empToSave.setUser(userToSave);
        empToSave.setDepartment(depart);
        empToSave.setEmployeeRoles(roles2);
        savedEmp = empService.createEmployee(empToSave, false);
        if (savedEmp.getId() != null) {
            listIDs.add(savedEmp.getId());
            updateRoles(allRoles, savedEmp.getEmployeeRoles());
        }
    }

    @Test
    public void findByUserLogin() {
        EmployeeDTO readEmployee = empAuthenticationService.findByUserLogin("krosh");
        if (readEmployee != null) {
            assertEquals("Krosh", readEmployee.getName());
            assertEquals(3, readEmployee.getEmployeeRoles().size());
        }
    }

    @Test
    public void findByUserLoginNotFoundException() {
        assertThrows(EntityNotFoundException.class, () ->
                        empAuthenticationService.findByUserLogin("bibi"),
                "EntityNotFoundException was expected.");
    }

    @Test
    public void loadUserByUsername() {
        UserDetails readUser = empAuthenticationService.loadUserByUsername("krosh");
        if (readUser != null) {
            assertEquals("krosh", readUser.getUsername());
            assertTrue(readUser.getAuthorities().toString().contains("user"));
            assertTrue(readUser.getAuthorities().toString().contains("user"));
            assertEquals(3, readUser.getAuthorities().size());
            assertTrue(encoder.matches("kroshpwd", readUser.getPassword()));
        }
    }

    @AfterEach
    public void tearDown() {
        for (Long id : listIDs) {
            assertTrue(empService.deleteEmployeeById(id));
        }
        listIDs.clear();
        // delete all users except "admin"
        userRepository.deleteByLoginNot(adminLogin);
        departRepository.deleteByNameNot(defaultDepartment);
        roleRepository.deleteAllExceptForSecurity();
    }

    private void updateRoles(RoleDTO[] allRoles, Set<RoleDTO> savedRoles) {
        for (RoleDTO savedRole : savedRoles) {
            for (RoleDTO role : allRoles) {
                if (role.getRole().equals(savedRole.getRole())) {
                    role.setId(savedRole.getId());
                    break;
                }
            }
        }
    }*/
}
