package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
// import com.qualcomm.robotcore.util.ElapsedTime;



@Autonomous(name = "Omnidirectional Encoders")
public class OmniEncoders extends LinearOpMode {
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;  
    float diagonal_distance;
    
  @Override
  public void runOpMode() {
    frontLeft = hardwareMap.get(DcMotor.class, "fl");
    frontRight = hardwareMap.get(DcMotor.class, "fr");
    backLeft = hardwareMap.get(DcMotor.class, "bl");
    backRight = hardwareMap.get(DcMotor.class, "br");

    frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

//    right.setDirection(DcMotorSimple.Direction.REVERSE);

    // frontLeftPos = 0;
    // frontRightPos = 0;
    // backLeftPos = 0;
    // backRightPos = 0;

    waitForStart();

    drive(1000, 1000, 0.25);
    drive(1000, -1000, 0.25);
  }

  private void drive(int x, int y, double speed) {

    // calculate encoder stuff
    // diagonal_distance = Math.sqrt(x ** 2 + y ** 2);

    // find angle
    

    // frontLeftPos += ;
    // frontRightPos += ;
    // backLeftPos += ;
    // backRightPos += ;

    
//    leftPos += leftTarget;
//    rightPos += rightTarget;

// !!!!!!
/*
    left.setTargetPosition(leftPos);
    right.setTargetPosition(rightPos);

    frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    
    left.setPower(speed);
    right.setPower(speed);

    while ( opModeIsActive() && left.isBusy() && right.isBusy() ) {
      idle();
    }
   */
  }
  
}
