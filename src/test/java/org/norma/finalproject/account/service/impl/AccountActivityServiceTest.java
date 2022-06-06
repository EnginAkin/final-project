package org.norma.finalproject.account.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.norma.finalproject.account.entity.base.Account;
import org.norma.finalproject.account.entity.base.AccountActivity;
import org.norma.finalproject.account.repository.AccountActivityRepository;
import org.norma.finalproject.common.entity.enums.ActionStatus;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AccountActivityServiceTest {

    @Mock
    private AccountActivityRepository activityRepository;

    @InjectMocks
    private AccountActivityServiceImpl underTest;

    @BeforeEach
    public void setup() {
        activityRepository.deleteAll();
    }

    @Test
    public void givenAccountId_whenGetAccountActivitiesByAccountID_thenReturnsActivitiesList() {
        // given
        Account account = new Account();
        account.setId(1L);
        account.setAccountName("account name");
        account.setAccountNo("11111");

        AccountActivity activity1 = new AccountActivity();
        activity1.setAccount(account);
        activity1.setDescription("description");
        activity1.setActionStatus(ActionStatus.INCOMING);

        AccountActivity activity2 = new AccountActivity();
        activity2.setAccount(account);
        activity2.setDescription("description2");
        activity2.setActionStatus(ActionStatus.INCOMING);
        List<AccountActivity> activities = List.of(activity1, activity2);

        BDDMockito.given(activityRepository.findAllByAccount_Id(account.getId())).willReturn(activities);
        // when
        List<AccountActivity> responseActivities = underTest.getAccountActivitiesByAccountId(account.getId());

        // then
        Assertions.assertThat(responseActivities.size()).isEqualTo(2);
    }

    @Test
    public void givenInvalidAccountId_whenGetAccountActivitiesByAccountID_thenReturnsActivitiesList() {
        // given
        Long invalidAccountId = 0L;
        BDDMockito.given(activityRepository.findAllByAccount_Id(invalidAccountId)).willReturn(List.of());
        // when
        List<AccountActivity> responseActivities = underTest.getAccountActivitiesByAccountId(invalidAccountId);
        // then
        Assertions.assertThat(responseActivities.size()).isEqualTo(0);
    }
}
