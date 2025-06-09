//A solid 99% of this was made by me just googling and asking ChatGPT how to do certain things so use at your own risk
package org.firstinspires.ftc.teamcode.LO2;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous
public class SummerAuto extends OpMode {
    private DcMotorEx frontLeft;
    private DcMotorEx frontRight;
    private DcMotorEx backRight;
    private DcMotorEx backLeft;

    // CONSTANTS: change these to  robot specs

    // Encoder ticks per motor revolution (hardware-specific)
    final double TICKS_PER_REV = 537.6;

    // Wheel diameter in centimeters
    final double WHEEL_DIAMETER = 9.5;

    // If each corner of the robot was a wheel what would the specs be
    final double ROBOT_WIDTH = 39;
    final double ROBOT_LENGTH = 33;

    // Calculate cm traveled per encoder tick:
    // Equation: cm_per_tick = (π × wheel_diameter) / TICKS_PER_REV
    final double CM_PER_TICK = (Math.PI * WHEEL_DIAMETER) / TICKS_PER_REV;

    // Calculate the turn radius (distance from robot center to wheel):
    // Equation: turn_radius = sqrt(width² + length²) / 2
    final double TURN_RADIUS = Math.sqrt(Math.pow(ROBOT_WIDTH, 2) + Math.pow(ROBOT_LENGTH, 2)) / 2;

    // Calculate cm the wheels must travel to turn robot 1 degree:
    // Equation: cm_per_degree = (2 × π × turn_radius) / 360
    final double CM_PER_DEGREE = (2 * Math.PI * TURN_RADIUS) / 360;

    // Max velocity of the motor in ticks per second (adjust for  motors)
    final double MAX_TICKS_PER_SECOND = 2300;

    @Override
    public void init() {
        frontLeft = hardwareMap.get(DcMotorEx.class, "fl");
        frontRight = hardwareMap.get(DcMotorEx.class, "fr");
        backLeft = hardwareMap.get(DcMotorEx.class, "bl");
        backRight = hardwareMap.get(DcMotorEx.class, "br");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addLine("Initiated");
        telemetry.update();
    }

    // takes y (forward), rx (rotation), x (strafe) powers between -1 and 1
    public void driveOmni(double y, double rx, double x) {
        double maxValue = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);

        double flPower = (y + x + rx) / maxValue;
        double blPower = (y - x + rx) / maxValue;
        double frPower = (y - x - rx) / maxValue;
        double brPower = (y + x - rx) / maxValue;

        frontLeft.setVelocity(flPower * MAX_TICKS_PER_SECOND);
        backLeft.setVelocity(blPower * MAX_TICKS_PER_SECOND);
        frontRight.setVelocity(frPower * MAX_TICKS_PER_SECOND);
        backRight.setVelocity(brPower * MAX_TICKS_PER_SECOND);
    }

    // Move  a given distance (cm) + is forward - is back
    public void move(double distanceCm) {
        // Calculate encoder ticks needed for desired distance
        int targetTicks = (int)(distanceCm / CM_PER_TICK);

        // Reset encoders before movement
        resetEncoders();

        // Set motors to run to target position
        frontLeft.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(targetTicks);
        frontRight.setTargetPosition(targetTicks);
        backRight.setTargetPosition(targetTicks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power forward
        frontLeft.setPower(0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backRight.setPower(0.5);

        // Wait until motors reach the target position
        while (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {
            //view movement
            telemetry.addData("Moving forward", "Ticks %d/%d", frontLeft.getCurrentPosition(), targetTicks);
            telemetry.update();
        }

        stopMotors();
    }

    // Strafe a given distance (cm) (+ is right - is left)
    public void strafe(double distanceCm) {
        int targetTicks = (int)(distanceCm / CM_PER_TICK);

        resetEncoders();

        // Set targets for strafing
        frontLeft.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(-targetTicks);
        frontRight.setTargetPosition(-targetTicks);
        backRight.setTargetPosition(targetTicks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Power to strafe right
        frontLeft.setPower(0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backRight.setPower(0.5);

        while (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {
            telemetry.addData("Strafing", "Ticks %d/%d", frontLeft.getCurrentPosition(), targetTicks);
            telemetry.update();
        }

        stopMotors();
    }

    // Rotate robot by degrees (+ for clockwise, - for counterclockwise)
    public void rotate(double degrees) {
        // Calculate distance each wheel must travel to rotate desired degrees
        double distanceCm = degrees * CM_PER_DEGREE;

        // Calculate ticks for this rotation distance
        int targetTicks = (int)(distanceCm / CM_PER_TICK);

        resetEncoders();

        // Set motor targets for rotation (opposite directions on left/right wheels)
        frontLeft.setTargetPosition(targetTicks);
        backLeft.setTargetPosition(targetTicks);
        frontRight.setTargetPosition(-targetTicks);
        backRight.setTargetPosition(-targetTicks);

        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set motor power for rotation
        frontLeft.setPower(0.5);
        backLeft.setPower(0.5);
        frontRight.setPower(0.5);
        backRight.setPower(0.5);

        while (frontLeft.isBusy() && backLeft.isBusy() && frontRight.isBusy() && backRight.isBusy()) {
            telemetry.addData("Rotating", "Ticks %d/%d", frontLeft.getCurrentPosition(), targetTicks);
            telemetry.update();
        }

        stopMotors();
    }

    // Utility method: stop all motors
    public void stopMotors() {
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    // Utility method: reset encoders to zero
    public void resetEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    @Override
    public void start() {
        telemetry.addLine("Auto start");
        telemetry.update();

        // test autonomous routine:

        move(50);
        strafe(30);
        rotate(90);
        move(20);
        rotate(-45);

        telemetry.addLine("Auto complete");
        telemetry.update();
    }

    @Override
    public void loop() {
        // Autonomous runs everything in start()
    }

    @Override
    public void stop() {
        stopMotors();
        telemetry.addLine("Stopped");
        telemetry.update();
    }
}
