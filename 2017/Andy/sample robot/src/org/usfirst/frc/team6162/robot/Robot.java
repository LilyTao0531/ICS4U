package org.usfirst.frc.team6162.robot;             

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.SPI.*;

public class Robot extends SampleRobot {
	NetworkTable table;
	RobotDrive myRobot = new RobotDrive(0,1,2,3);
	Joystick XboxController = new Joystick(0);
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	SendableChooser<String> chooser = new SendableChooser<>();
	//private static final double kVoltsPerDegreePerSecond=0.0128;
	private SpeedController BallCollector = new Talon(4);
	private SpeedController Shooter = new Talon(5);
	private SpeedController Door = new Talon(7);
	private ADXRS450_Gyro gyro = new ADXRS450_Gyro(SPI.Port.kOnboardCS0);  		
	double angle = gyro.getAngle(); // get current heading
    double rate = gyro.getRate();//get rate of rotation
	
     
     public Robot() {
		myRobot.setExpiration(0.1);
	}

	
	public void robotInit() {
	
		
		table = NetworkTable.getTable("datatable");
		
		gyro.reset();
		gyro.calibrate();
		
		
	}

	
	
	public void autonomous() {		
		  String autoSelected = SmartDashboard.getString("Auto Selector",defaultAuto);
		  System.out.println("Auto selected: " + autoSelected);
		  gyro.reset();
		  while (isAutonomous()) {
	        	
	           
			    angle = gyro.getAngle();
				table.putNumber("Angle", angle);
	            double kp=0.05;
	            /*myRobot.drive(0.2, 0.0);
	            Timer.delay(0.25);
	            myRobot.drive(0.2, -angle*kp); // drive towards heading 0
	            Timer.delay(0.25); */
	            while(angle<166){
	            	angle = gyro.getAngle();
					table.putNumber("Angle", angle);
	            	myRobot.drive(0.1+0.1*(180-angle)/180,1.0);
	            	Timer.delay(0.2);

	            }
	 	           myRobot.drive(0.1, 0.0);
		            Timer.delay(0.2);
	            
		  }
		  myRobot.drive(0.0, 0.0);
	}

	public void NetworkTable(){
	table = NetworkTable.getTable("");
	}
	
	
	public void driverStationComm(){
		table.putNumber("ShowAngle",angle);
	}
		
	
	public void operatorControl() {// X BOX BOTTON SWITCHES)
		myRobot.setSafetyEnabled(true);
		while (isOperatorControl() && isEnabled()) {
			
			angle = gyro.getAngle();
			table.putNumber("Angle", angle);
			myRobot.arcadeDrive(XboxController); // drive with arcade style (use right
			if(XboxController.getRawAxis(3)!=0){
			Door.set(XboxController.getRawAxis(3));
		    }
		    else{
			Door.set(0);	
				}
			if(XboxController.getRawAxis(4)!=0){
			BallCollector.set(XboxController.getRawAxis(4));	
			}
			else{
			BallCollector.set(0);
			}
			if(XboxController.getRawAxis(2)!=0){
		    Shooter.set(XboxController.getRawAxis(2));
			}
			else{
		    Shooter.set(0);	
			}
		    /*if(XboxController.getRawButton(3)==true && XboxController.getRawButton(4)==false){
			Door.set(1);
			}
			else{
			Door.set(0);
			}
		    if(XboxController.getRawButton(4)==true && XboxController.getRawButton(3)==false){
		    Door.set(-1);
		    }
		    else{
		    Door.set(0);		
		    }*/
		    
		    
		}
		//if not enabled, reset gyro
		while (isOperatorControl()&&!isEnabled()){
			gyro.reset();
			gyro.calibrate();
		}
		
		
			Timer.delay(0.005); // wait for a motor update time
		
			double x =0 ;
			double y = 0 ;
			while(isOperatorControl() && isEnabled()){
				Timer.delay(0.25);
				table.putNumber("X", x);
				table.putNumber("Y", y);
				x += 0.05;
				y += 1.00;
			}
	}
	

	public void test() {
	}
}
