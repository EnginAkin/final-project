package org.norma.finalproject.common.core.utils;

public class Messages {

    public static final String CHECKING_ACCOUNT_ALREADY_HAVE_ACCOUNT_SAME_EXCEPTION = "You have a already bank account in this bank same currency and same branch .";
    public static final String CHECKING_ACCOUNT_BLOCKED_SUCCESSFULLY = "Customer Blocked successfull. Customer cannot transfer anymore.";
    public static final String CHECKING_ACCOUNT_NOT_FOUND = "Checking Account not Found.";
    public static final String ACCOUNT_HAS_BALANCE_DELETE_EXCEPTION = "Balance greater than 0 in account.Cannot be deleted.";
    public static final String CHECKING_ACCOUNT_DELETED_SUCCESSFULLY = "Deleted successfully Checking account.";
    public static final String SAVING_ACCOUNT_OPERATION_NOT_MATCHED_CURRENCY_TYPE_EXCEPTION = "Parent currency type not matched for saving account,Please create checking Account currency type :";
    public static final String SAVING_ACCOUNT_OPERATION_PARENT_USED_FOR_SAVING_ACCOUNT_EXCEPTION = "Parent Used for saving account , change parent checking account.";
    public static final String SAVING_ACCOUNT_OPERATION_PARENT_BALANCE_NOT_ENOUGH_EXCEPTION = "Parent account balance not enough for saving balance.";
    public static final String SAVING_ACCOUNT_OPERATION_ACTIVITIES_NOT_FOUND_EXCEPTION = "Parent account balance not enough for saving balance.";
    public static final String DEBIT_CARD_OPERATION_ONE_ACCOUNT_MUST_ONE_CARD_EXCEPTION = "An account must have only one card.";
    private Messages() {
        throw new UnsupportedOperationException();
    }


}
