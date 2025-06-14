// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// import edu.wpi.first.util.sendable.SendableRegistry;
// import edu.wpi.first.wpilibj.Joystick;
// import edu.wpi.first.wpilibj.Trigger;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
// import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.wpilibj.XboxController;

/**
 * This is a demo program showing the use of the DifferentialDrive class, specifically it contains
 * the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private final DifferentialDrive m_robotDrive;
  private final XboxController m_controller;

  private final SparkMax m_leftMotor1 = new SparkMax(1, MotorType.kBrushed);
  private final SparkMax m_leftMotor2 = new SparkMax(2, MotorType.kBrushed);
  private final SparkMax m_rightMotor3 = new SparkMax(3, MotorType.kBrushed);
  private final SparkMax m_rightMotor4 = new SparkMax(4, MotorType.kBrushed);

  private final SparkMaxConfig leftConfig = new SparkMaxConfig();
  private final SparkMaxConfig rightConfig = new SparkMaxConfig();

  /** Called once at the beginning of the robot program. */
  public Robot() {
    // We need to invert one side of the drivetrain so that positive voltages
    // result in both sides moving forward. Depending on how your robot's
    // gearbox is constructed, you might have to invert the left side instead.
    
    // m_rightMotor4.SparkBaseConfig.inverted(true);

    leftConfig.follow(2, false);
    rightConfig.follow(4, true);

    m_leftMotor1.configure(leftConfig, null, null);
    m_rightMotor3.configure(rightConfig, null, null);

    m_robotDrive = new DifferentialDrive(m_leftMotor2::set, m_rightMotor4::set);
    m_controller = new XboxController(0);
  }

  @Override
  public void teleopPeriodic() {
    m_robotDrive.tankDrive(m_controller.getLeftY(), m_controller.getRightY());
  }
}