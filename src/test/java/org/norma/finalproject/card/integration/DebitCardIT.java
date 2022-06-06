package org.norma.finalproject.card.integration;

/*
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DebitCardIT {
    //TODO daha sonra yapılacak.

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CheckingAccountRepository checkingAccountRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private DebitCardRepository debitCardRepository;
    @Autowired
    private PasswordEncoder encoder;
    private Customer customer=new Customer();
    private String tokenValue;
    private CheckingAccount checkingAccount;

    @BeforeEach
    public  void setup() {
        customer.setIdentityNumber("11111111111");
        customer.setUserNumber("11111111111");
        customer.setTelephone("0000000");
        customer.setPassword(encoder.encode("000000"));
        customer.setName("TEST-FOR-SECURITY");
        customer.setSurname("TEST-FOR-SECURITY");
        customer.setEmail("TEST-FOR-SECURITY");
        Role role = new Role(CustomerConstant.ROLE_USER);
        roleRepository.save(role);
        customer.setRoles(Set.of(role));
        checkingAccount=createCheckingAccount();
        customer.setCheckingAccounts(List.of(checkingAccount));
        customerRepository.save(customer);
        checkingAccount.setCustomer(customer);
        checkingAccountRepository.save(checkingAccount);

        tokenValue = JWT.create()
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + 300000))
                .withSubject(customer.getIdentityNumber())
                .withClaim("roles", CustomerConstant.ROLE_USER)
                .sign(Algorithm.HMAC512("normasecretkey"));
        JWTToken token = new JWTToken();
        token.setToken(tokenValue);
        tokenRepository.save(token);

    }


    @Test
    public void accessCreateDebitCardUnauthorizedWithoutJwtToken() throws Exception {
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cards/debits")
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void givenCreateDebitCardRequest_whenCreate_thenReturn201() throws Exception {
        // given

        CreateDebitCardRequest debitCardRequest=new CreateDebitCardRequest();
        debitCardRequest.setParentCheckingAccountId(checkingAccount.getId());
        debitCardRequest.setPassword("1111");
        // when
        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/cards/debits")
                        .header("authorization", "Bearer "+tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(debitCardRequest))
        );
        // then
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));
    }
/*
    @Test
    public void givenCardId_whenDeleteDebitCardById_thenReturn200() throws Exception {
        DebitCard debitCard=new DebitCard();
        debitCard.setCheckingAccount(checkingAccount);
        debitCard.setCardNumber("11111");
        debitCard.setCvv("11111");
        debitCard.setStatus(CardStatus.ACTIVE);
        debitCard.setPassword("1111");
        debitCard.setExpiryDate(new Date());
        debitCardRepository.save(debitCard);

        ResultActions actions = mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/v1/cards/debits/"+debitCard.getId())
                        .header("authorization", "Bearer "+tokenValue)
        );
        actions.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccessful", CoreMatchers.is(true)));
    }





    private CheckingAccount createCheckingAccount() {

        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setAccountNo("1111");
        checkingAccount.setIbanNo("1111");
        checkingAccount.setBranchCode("00");
        checkingAccount.setBlocked(false);
        checkingAccount.setBankCode("000000");
        checkingAccount.setAccountName("account name");
        checkingAccount.setBranchName("FATIH");
        return checkingAccount;
    }
}
 */

