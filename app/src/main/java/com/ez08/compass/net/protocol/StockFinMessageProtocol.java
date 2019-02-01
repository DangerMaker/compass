package com.ez08.compass.net.protocol;

/*
 * msg.push.*** 5001~5020;
 * msg.info.key_indicators 5021~5050;
 */
public class StockFinMessageProtocol {
	public static final String proto_znz_stockfindata = "message msg.info.key_indicators(5021){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required double price_earning_ratio = 3;" +
			"required double price_to_book_ratio = 4;" +
			"required double basic_earnings_per_share = 5;" +
			"required double book_value_per_share = 6;" +
			"required double operating_revenue = 7;" +
			"required double net_profit = 8;" +
			"required double debt_asset_ratio = 9;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.income_statement(5022){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required double gross_revenue = 3;" +
			"required double total_operating_cost = 4;" +
			"required double operating_cost = 5;" +
			"required double operating_tax_and_additions = 6;" +
			"required double selling_expense = 7;" +
			"required double general_and_administrative_expense = 8;" +
			"required double financial_expense = 9;" +
			"required double assets_impairment_loss = 10;" +
			"required double profits_from_changes_in_fair_value = 11;" +
			"required double investment_income = 12;" +
			"required double exchange_gain = 13;" +
			"required double operating_income = 14;" +
			"required double non_operating_income = 15;" +
			"required double non_operating_expense = 16;" +
			"required double income_before_tax = 17;" +
			"required double income_tax = 18;" +
			"required double net_profit = 19;" +
			"required double net_income_attributed_to_shareholders = 20;" +
			"required double minority_interest = 21;" +
			"required double recurrent_net_profit = 22;" +
			"required double basic_earnings_per_share = 23;" +
			"required double diluted_earning_per_share = 24;" +
			"required double other_comprehensive_income = 25;" +
			"required double total_comprehensive_income = 26;" +
			"required double total_income_attributed_to_parent_company = 27;" +
			"required double total_income_attributed_to_minority_shareholders = 28;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.balance_sheet(5023){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required double cash = 3;" +
			"required double financial_assets_held_for_trading = 4;" +
			"required double notes_receivable = 5;" +
			"required double accounts_receivable = 6;" +
			"required double accounts_prepaid = 7;" +
			"required double interest_receivable = 8;" +
			"required double dividends_receivable = 9;" +
			"required double other_receivables = 10;" +
			"required double inventories = 11;" +
			"required double current_portion_of_non_current_assets = 12;" +
			"required double total_current_assets = 13;" +
			"required double available_for_sale_financial_assets = 14;" +
			"required double held_to_maturity_investment = 15;" +
			"required double long_term_account_receivable = 16;" +
			"required double long_term_equity_investments = 17;" +
			"required double investment_property = 18;" +
			"required double fixed_assets = 19;" +
			"required double construction_in_progress = 20;" +
			"required double intangible_assets = 21;" +
			"required double development_expenditure = 22;" +
			"required double goodwill = 23;" +
			"required double long_term_prepaid_expense = 24;" +
			"required double deferred_income_tax_assets = 25;" +
			"required double total_non_current_assets = 26;" +
			"required double total_assets = 27;" +
			"required double short_term_borrowings = 28;" +
			"required double financial_liabilities_held_for_trading = 29;" +
			"required double note_payable = 30;" +
			"required double accounts_payable = 31;" +
			"required double advance_receipts = 32;" +
			"required double accrued_payroll = 33;" +
			"required double accrued_tax = 34;" +
			"required double interest_payable = 35;" +
			"required double dividends_payable = 36;" +
			"required double other_payables = 37;" +
			"required double current_portion_of_non_current_liabilities = 38;" +
			"required double total_current_liabilities = 39;" +
			"required double long_term_borrowings = 40;" +
			"required double debentures_payable = 41;" +
			"required double long_term_accounts_payable = 42;" +
			"required double payables_for_specific_projects = 43;" +
			"required double provision_for_liabilities = 44;" +
			"required double deferred_income_tax_liabilities = 45;" +
			"required double total_non_current_liabilities = 46;" +
			"required double total_liabilities = 47;" +
			"required double capital = 48;" +
			"required double capital_reserve = 49;" +
			"required double surplus_reserve = 50;" +
			"required double undistributed_profits = 51;" +
			"required double total_equity_attributable_to_equity_holders_of_the_company = 52;" +
			"required double minority_equity = 53;" +
			"required double total_owners_equity = 54;" +
			"required double total_liabilities_and_owners_equity = 55;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.cash_flow_statement(5024){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required double cash_received_from_selling_goods = 3;" +
			"required double refunds_of_taxes = 4;" +
			"required double other_cash_received_related_to_operating_activities = 5;" +
			"required double subtotal_of_cash_inflows_related_to_operating_activities = 6;" +
			"required double cash_paid_for_goods_and_services = 7;" +
			"required double cash_paid_to_and_for_employees = 8;" +
			"required double payments_of_all_type_of_taxes = 9;" +
			"required double other_cash_paid_related_to_operating_activities = 10;" +
			"required double subtotal_of_cash_outflows_related_to_operating_activities = 11;" +
			"required double net_cash_flows_from_operating_activities = 12;" +
			"required double cash_received_from_disposal_of_investments = 13;" +
			"required double cash_received_from_return_on_investments = 14;" +
			"required double net_cash_received_from_disposal_of_fixed_assets = 15;" +
			"required double net_cash_received_from_disposal_of_subsidiaries = 16;" +
			"required double other_cash_received_related_to_investing_activities = 17;" +
			"required double subtotal_of_cash_inflows_related_to_investing_activities = 18;" +
			"required double cash_paid_to_acquiring_fixed_assets = 19;" +
			"required double cash_paid_for_investments = 20;" +
			"required double net_cash_outflows_of_procurement_of_subsidiaries = 21;" +
			"required double other_cash_paid_related_to_investing_activities = 22;" +
			"required double subtotal_of_cash_outflows_related_to_investing_activities = 23;" +
			"required double net_cash_flows_from_investing_activities = 24;" +
			"required double cash_received_from_borrowings = 25;" +
			"required double other_cash_received_related_to_financing_activities = 26;" +
			"required double subtotal_of_cash_inflows_related_to_financing_activities = 27;" +
			"required double cash_paid_for_debt = 28;" +
			"required double cash_paid_for_dividend = 29;" +
			"required double other_cash_paid_related_to_financing_activities = 30;" +
			"required double subtotal_of_cash_outflows_related_to_financing_activities = 31;" +
			"required double net_cash_flows_from_financing_activities = 32;" +
			"required double effect_of_foreign_exchange_rate_changes_on_cash = 33;" +
			"required double net_increase_of_cash_and_cash_equivalents = 34;" +
			"required double cash_and_cash_equivalents_at_the_beginning_of_the_period = 35;" +
			"required double cash_and_cash_equivalents_at_the_end_of_the_period = 36;" +
			"required double net_profit = 37;" +
			"required double provision_for_impairment_of_fixed_assets = 38;" +
			"required double depreciation_of_fixed_assets = 39;" +
			"required double amortization_of_intangible_assets = 40;" +
			"required double amortization_of_long_term_prepayment = 41;" +
			"required double losses_on_disposal_of_fixed_assets = 42;" +
			"required double losses_on_scrapping_fixed_assets = 43;" +
			"required double losses_on_the_changes_in_fair_value = 44;" +
			"required double financial_expense = 45;" +
			"required double investment_losses = 46;" +
			"required double decrease_in_deferred_tax_assets = 47;" +
			"required double increase_in_deferred_tax_assets = 48;" +
			"required double decrease_in_inventories = 49;" +
			"required double decrease_in_operating_receivables = 50;" +
			"required double increase_in_operating_receivables = 51;" +
			"required double net_cash_flows_from_operating_activities_2 = 52;" +
			"required double cash_at_the_end_of_the_period = 53;" +
			"required double cash_at_the_beginning_of_the_period = 54;" +
			"required double cash_equivalents_at_the_end_of_the_period = 55;" +
			"required double cash_equivalents_at_the_beginning_of_the_period = 56;" +
			"required double net_increase_of_cash_and_cash_equivalents_2 = 57;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.company_profile(5025){" +
			"required string stock_code = 1;" +
			"required string company_name = 2;" +
			"required string company_name_abbreviation = 3;" +
			"required string english_name = 4;" +
			"required string english_name_abbreviation = 5;" +
			"required string legal_representative = 6;" +
			"required string registered_address = 7;" +
			"required string office_address = 8;" +
			"required string post_code = 9;" +
			"required double registered_capital = 10;" +
			"required string currency_code = 11;" +
			"required string currency_name = 12;" +
			"required string date_of_establishment = 13;" +
			"required string website_address = 14;" +
			"required string email = 15;" +
			"required string phone_number = 16;" +
			"required string fax = 17;" +
			"required string main_business = 18;" +
			"required string business_scope = 19;" +
			"required string business_nature_code = 20;" +
			"required string business_nature = 21;" +
			"required string brief_introduction = 22;" +
			"required string tax_id = 23;" +
			"required string business_license_number = 24;" +
			"required string information_newspaper = 25;" +
			"required string information_newspaper_code = 26;" +
			"required string information_website = 27;" +
			"required string information_website_code = 28;" +
			"required string board_secretary = 29;" +
			"required string board_secretary_phone_number = 30;" +
			"required string board_secretary_fax = 31;" +
			"required string board_secretary_email = 32;" +
			"required string securities_affairs_representative = 33;" +
			"required string market_status_code = 34;" +
			"required string market_status = 35;" +
			"required string province_code = 36;" +
			"required string province = 37;" +
			"required string city_code = 38;" +
			"required string city = 39;" +
			"required string district = 40;" +
			"required string industry_code = 41;" +
			"required string industry_primary_category = 42;" +
			"required string industry_secondary_category = 43;" +
			"required string industry_big_category = 44;" +
			"required string industry_mid_category = 45;" +
			"required string accounting_firm = 46;" +
			"required string law_firm = 47;" +
			"required string general_manager = 48;" +
			"required double offering_price = 49;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.equity_report(5026){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required string report_source = 3;" +
			"required double total_shares = 4;" +
			"required double circulating_shares = 5;" +
			"required double b_shares = 6;" +
			"required double h_shares = 7;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.shareholders_report(5027){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required int64 total_shareholders = 3;" +
			"required int64 total_a_shareholders = 4;" +
			"required int64 total_b_shareholders = 5;" +
			"required int64 total_h_shareholders = 6;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.top_ten_shareholders(5028){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required int32 shareholder_order = 3;" +
			"required string shareholder_name = 4;" +
			"required string shareholder_id = 5;" +
			"required string shareholder_class_id = 6;" +
			"required string shareholder_class = 7;" +
			"required int64 amount_of_share_holding = 8;" +
			"required double rate_of_share_holding = 9;" +
			"required string nature_of_shareholder_code = 10;" +
			"required string nature_of_shareholder = 11;" +
			"required string update_time = 99;" +
			"}" +
			"message msg.info.top_ten_tradable_shareholders(5029){" +
			"required string stock_code = 1;" +
			"required string report_date = 2;" +
			"required int32 shareholder_order = 3;" +
			"required string shareholder_name = 4;" +
			"required string shareholder_id = 5;" +
			"required string shareholder_class_id = 6;" +
			"required string shareholder_class = 7;" +
			"required int64 amount_of_share_holding = 8;" +
			"required double rate_of_share_holding = 9;" +
			"required string nature_of_shareholder_code = 10;" +
			"required string nature_of_shareholder = 11;" +
			"required string update_time = 99;" +
			"}" ;
}
