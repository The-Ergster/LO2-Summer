package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;



@Autonomous(name = "Omnidirectional Encoders")
public class OmniEncoders extends LinearOpMode {
  private DcMotor left;
  private DcMotor right;
  
  
  @Override
  public void runOpMode() {
    left = harwareMap.get(DcMotor.class, "leftMotor");
    right = harwareMap.get(DcMotor.class, "rightMotor");

    left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    right.setDirection(DcMotorSimple.Direction.REVERSE);

    leftPos = 0;
    rightPos = 0;

    waitForStart();

    drive(1000, 1000, 0.25);
      drive(1000, -1000, 0.25);
  }

  private void drive(int leftTarget, int rightTarget, double speed) {
    leftPos += leftTarget;
    rightPos += rightTarget;

    left.setTargetPosition(leftPos);
    right.setTargetPosition(rightPos);

    left.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    right.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    left.setPower(speed);
    right.setPower(speed);

    while ( opModeIsActive() && left.isBusy() && right.isBusy() ) {
      idle();
    }
  }
  
}
