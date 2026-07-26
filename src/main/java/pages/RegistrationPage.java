package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import base.BasePage;

public class RegistrationPage extends BasePage
{
	public RegistrationPage(WebDriver driver)
	{
		super(driver);
	}
	
		private By name = By.xpath("//input[@placeholder='Name']");
		private By email = By.xpath("//input[@placeholder='Email']");
		private By password = By.xpath("//input[@placeholder='Password']");
		
		// Interests checkboxes
		private By jmeter = By.xpath("//label[text()='JMeter']/preceding-sibling::*");
		private By java = By.xpath("//label[text()='Java']/preceding-sibling::*");
		private By JavaScript = By.xpath("//label[text()='JavaScript']/preceding-sibling::*");
		private By TestNG = By.xpath("//label[text()='TestNG']/preceding-sibling::*");
		
		private By stateDropdown = By.xpath("//select[@name='state']");
		
		public void selectState(String state){
			Select select=new Select(driver.findElement(stateDropdown));
			select.selectByVisibleText(state);
		}
		
		
		
		
		
	    


	
}
