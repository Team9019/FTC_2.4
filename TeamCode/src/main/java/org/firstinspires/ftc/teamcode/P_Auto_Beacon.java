package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
FUNCTION:
    Autonomous: Uses encoders only

    Steps:
        <optional delay>
        Drive within shooting distance
        Shoot ball(s)
        Drive to align with first beacon
        Turn 45 degrees to aim to beacon
        Drive into beacon
        <back up 3 inches>
        <sense beacon, taking action, based on result>
        Stop
 */

@Autonomous(name="3 Beacon (Encoders)", group="Autonomous")
@Disabled
public class P_Auto_Beacon extends LinearOpMode {
    private Hardware robot = new Hardware(telemetry);
    private Configuration configs = new Configuration(telemetry);
    private Commands cmds = new Commands(telemetry, this);
    private Initialize init = new Initialize(telemetry);

    @Override
    public void runOpMode() throws InterruptedException
    {
        telemetry.addData("BEGIN", "Autonomous Starting...");
        telemetry.update();

        robot.init(hardwareMap);

        configs.loadParameters();

        //cmds.InitializeHW(robot);
        init.InitializeHW(robot);

        telemetry.addData("Config", "Configured for " + Configuration.ALLIANCE + " Alliance.");
        telemetry.addData("Config", "Configured for " + Configuration.START_POSITION + " Starting Position.");
        telemetry.update();

        waitForStart();

        //Wait for alliance moves or to avoid penalty for early cross
        telemetry.addData("Status", "Delay before driving ...");
        telemetry.update();

        sleep(Configuration.TIME_AUTO_DELAY);

        telemetry.addData("Status", "Delay Complete!");
        telemetry.update();

        //Move close enough to shoot balls
        if (Configuration.START_POSITION.equals("LONG"))
        {
            cmds.EncoderDrive(robot, Configuration.POWER_DRIVE, Configuration.DIST_CORNER_TO_SHOOT, Configuration.DIST_CORNER_TO_SHOOT, 5.0);
        }
        else //SHORT
        {
            cmds.EncoderDrive(robot, Configuration.POWER_DRIVE, Configuration.DIST_SIDE_TO_SHOOT, Configuration.DIST_SIDE_TO_SHOOT, 5.0);
        }

        robot.motorLaunch.setPower(Configuration.POWER_LAUNCH);

        //Use delay until ball launch is ready for use
        sleep(2000);

        robot.motorCollect.setPower(1.0);

        cmds.Shoot(robot);
        //cmds.Shoot(robot);

        robot.motorCollect.setPower(0);

        //Drive to line up with closest beacon
        if (Configuration.START_POSITION.equals("LONG"))
        {
            cmds.EncoderDrive(robot, Configuration.POWER_DRIVE, Configuration.DIST_LONG_BEACON1_AIM, Configuration.DIST_LONG_BEACON1_AIM, 5.0);
        }
        else
        {
            cmds.EncoderDrive(robot, Configuration.POWER_DRIVE, Configuration.DIST_SHORT_BEACON1_AIM, Configuration.DIST_SHORT_BEACON1_AIM, 5.0);
        }

        //Turn to face beacon
        if(Configuration.ALLIANCE.equals("RED"))
        {
            cmds.EncoderDrive(robot, Configuration.POWER_TURN, -Configuration.INCHES_FORTYFIVE_DEGREE_TURN, Configuration.INCHES_FORTYFIVE_DEGREE_TURN +1.5 , 5.0);
        }
        else    //BLUE
        {
            cmds.EncoderDrive(robot, Configuration.POWER_TURN, Configuration.INCHES_FORTYFIVE_DEGREE_TURN +1.5, -Configuration.INCHES_FORTYFIVE_DEGREE_TURN, 5.0);
        }

        //Drive into beacon, pressing button
        if (Configuration.START_POSITION.equals("LONG"))
        {
            cmds.EncoderDrive(robot, Configuration.POWER_DRIVE, Configuration.DIST_LONG_FIRST_BEACON, Configuration.DIST_LONG_FIRST_BEACON, 5.0);
        }
        else
        {
            cmds.EncoderDrive(robot, Configuration.POWER_DRIVE, Configuration.DIST_SHORT_FIRST_BEACON, Configuration.DIST_SHORT_FIRST_BEACON, 5.0);
        }

        //Backup 3 inches to assess color
        //cmds.EncoderDrive(robot,Configuration.APPROACH_SPEED, -3, -3, 5.0);

        //Sensing beacon will also invoke a 3 inch drive if the opposite color is detected
        cmds.SenseBeacon(robot);

        //Turn 90 degrees to drive to second beacon
        //if(Configuration.ALLIANCE.equals("RED"))
        //{
        //    cmds.EncoderDrive(robot, Configuration.TURN_POWER, Configuration.NINETY_DEGREE_TURN_INCHES, -Configuration.NINETY_DEGREE_TURN_INCHES, 3.0);
        //}
        //else    //BLUE
        //{
        //    cmds.EncoderDrive(robot, Configuration.TURN_POWER, -Configuration.NINETY_DEGREE_TURN_INCHES, Configuration.NINETY_DEGREE_TURN_INCHES, 3.0);
        //}

        //Drive to second beacon
        //cmds.EncoderDrive(robot, Configuration.DRIVE_POWER, 40, 40, 5.0);

        //Turn 90 degrees to face beacon
        //if(Configuration.ALLIANCE.equals("RED"))
        //{
        //    cmds.EncoderDrive(robot, Configuration.TURN_POWER, -Configuration.NINETY_DEGREE_TURN_INCHES, Configuration.NINETY_DEGREE_TURN_INCHES, 3.0);
        //}
        //else    //BLUE
        //{
        //    cmds.EncoderDrive(robot, Configuration.TURN_POWER, Configuration.NINETY_DEGREE_TURN_INCHES, -Configuration.NINETY_DEGREE_TURN_INCHES, 3.0);
        //}

        //Sensing beacon will also invoke a 3 inch drive if the opposite color is detected
//        cmds.SenseBeacon(robot);

        cmds.StopDriving(robot);

        telemetry.addData("Status", "Autonomous Complete!");
        telemetry.update();
    }
}
