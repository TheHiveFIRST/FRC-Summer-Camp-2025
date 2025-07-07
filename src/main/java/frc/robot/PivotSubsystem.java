package frc.robot;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import frc.robot.Constants; 

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


public class PivotSubsystem extends SubsystemBase { 
    
    private SparkMax pivotMotor; 
    private SparkMaxConfig pivotConfig; 
    private AbsoluteEncoder m_absoluteEncoder; 
    private PIDController m_PIDController; 
    private double pivotOutput;

    public PivotSubsystem() {
        pivotMotor = new SparkMax(0, MotorType.kBrushed); 
        pivotConfig = new SparkMaxConfig();
        pivotConfig.idleMode(IdleMode.kBrake);
        pivotMotor.configure(pivotConfig, null, null); 

        m_absoluteEncoder = pivotMotor.getAbsoluteEncoder(); 
        m_PIDController = new PIDController(Constants.pivotConstants.pivotKP,Constants.pivotConstants.pivotKI, Constants.pivotConstants.pivotKD);
    }

    public double encoderGetValue() {
        return m_absoluteEncoder.getPosition(); 
    } 

    public void pivotPIDControl(double targetAngle) {
        pivotOutput = m_PIDController.calculate(m_absoluteEncoder.getPosition(), targetAngle);
        pivotMotor.set(pivotOutput);  
    }
}
