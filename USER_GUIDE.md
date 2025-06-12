# User Guide - Hospital Management System

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
3. [Patient Portal](#patient-portal)
4. [Doctor Portal](#doctor-portal)
5. [Admin Panel](#admin-panel)
6. [Common Tasks](#common-tasks)
7. [Troubleshooting](#troubleshooting)
8. [FAQ](#faq)

## Introduction

Welcome to the Hospital Management System! This comprehensive guide will help you navigate and use all features of the system effectively. The system is designed for three types of users: Patients, Doctors, and Administrators.

### System Overview

The Hospital Management System provides:
- **Patient Management**: Registration, appointment booking, medical records
- **Doctor Management**: Appointment processing, patient directory, session tracking
- **Administrative Functions**: System oversight, user management, statistics
- **Data Security**: Secure login system with session management
- **Real-time Updates**: Live data synchronization across all modules

## Getting Started

### System Requirements

- **Display**: Minimum 1024x768 resolution
- **Memory**: 4GB RAM recommended
- **Storage**: 100MB free space
- **Java**: JRE 11 or higher
- **Network**: Not required (standalone application)

### First Launch

1. **Launch the Application**
   - Double-click the application icon or run via command line
   - The system will display the main login screen

2. **Choose User Type**
   - **Patient**: For booking appointments and accessing medical records
   - **Doctor**: For processing appointments and managing patients
   - **Admin**: For system administration and oversight

### Default Accounts

For testing and initial setup:

**Administrator**
- Username: `admin`
- Password: `admin123`

**Test Doctor**
- ID: `9814`
- Password: `password123`

**Test Patient**
- Username: `gibran_azmi`
- Password: `password123`

## Patient Portal

### Registration

#### New Patient Registration

1. **Access Registration**
   - Click "Register as New Patient" on the login screen
   - Fill out the registration form completely

2. **Required Information**
   - **Full Name**: Your complete legal name
   - **Username**: Unique identifier for login (letters and numbers only)
   - **Password**: Secure password (minimum 6 characters)
   - **Email**: Valid email address for communication
   - **Age**: Your current age
   - **Address**: Complete residential address
   - **Phone**: Contact phone number

3. **Complete Registration**
   - Click "Register Patient" button
   - System will generate a unique 7-digit Patient ID
   - Save your Patient ID for future reference

#### Important Notes
- **Patient ID**: Keep this safe - you'll need it for appointments
- **Username**: Must be unique across all patients
- **Password**: Use a strong password for security

### Login Process

1. **Enter Credentials**
   - Username: Your registered username
   - Password: Your account password

2. **Access Dashboard**
   - Successful login opens your patient dashboard
   - View your profile information and recent activity

### Dashboard Overview

The Patient Dashboard provides:

#### Header Information
- **Welcome Message**: Displays your name
- **Email**: Shows your registered email address
- **Status**: Current system status

#### Recent Activity Section
- **Latest Appointments**: Shows your 5 most recent appointments
- **Appointment Status**: Scheduled, completed, or cancelled
- **Doctor Information**: Shows which doctor you saw
- **Date/Time**: When appointments occurred

#### Quick Actions
- **Book Appointment**: Schedule new appointments
- **Medical Records**: View your complete medical history
- **Logout**: Safely exit your session

### Booking Appointments

#### Step 1: Select Doctor

1. **Navigate to Booking**
   - Click "Book Appointment" from dashboard
   - Or use the sidebar menu

2. **Choose Available Doctor**
   - System shows currently logged-in doctors only
   - Each doctor listing shows:
     - Doctor name and ID
     - Specialty area
     - Current availability status

3. **Doctor Selection**
   - Click on your preferred doctor
   - Review doctor's specialty to ensure it matches your needs

#### Step 2: Schedule Details

1. **Select Date**
   - Use the date picker to choose your preferred date
   - Only future dates are selectable
   - Consider doctor's availability schedule

2. **Choose Time**
   - Enter time in HH:mm format (24-hour)
   - Example: 14:30 for 2:30 PM
   - Check hospital hours: Mon-Fri 8AM-6PM, Sat 9AM-4PM, Sun Emergency Only

3. **Describe Illness/Concern**
   - Provide a clear description of your medical concern
   - Include relevant symptoms
   - Mention duration and severity
   - Be specific but concise

#### Step 3: Confirm Booking

1. **Review Details**
   - Verify doctor selection
   - Confirm date and time
   - Check illness description

2. **Complete Booking**
   - Click "Schedule Appointment"
   - System generates unique appointment ID
   - Note down your appointment ID for reference

3. **Confirmation**
   - Success message displays appointment details
   - Return to dashboard to see new appointment

#### Booking Tips
- **Plan Ahead**: Book appointments several days in advance
- **Be Available**: Ensure you're available at selected time
- **Prepare Information**: Have medical history and symptoms ready
- **Emergency Cases**: For emergencies, contact hospital directly

### Medical Records Access

#### Viewing Your Records

1. **Navigate to Records**
   - Click "Medical Records" from dashboard
   - Or use sidebar navigation

2. **Personal Information**
   - **Patient Details**: Name, ID, contact information
   - **Demographics**: Age, address, phone number
   - **Account Info**: Email, registration date

#### Medical History

1. **Illness History**
   - **Past Conditions**: Previous medical conditions
   - **Chronological Order**: Most recent first
   - **Condition Details**: Name and description of each condition

2. **Treatment Records**
   - **Doctor Notes**: Notes from previous consultations
   - **Treatment Plans**: Prescribed treatments
   - **Follow-up Requirements**: Any ongoing care needed

#### Appointment History

1. **Past Appointments**
   - **Complete List**: All previous appointments
   - **Doctor Information**: Which doctor you saw
   - **Date and Time**: When appointments occurred
   - **Reason**: Why you visited (illness/concern)
   - **Status**: Completed, cancelled, or no-show

2. **Upcoming Appointments**
   - **Scheduled Visits**: Future appointments
   - **Preparation Notes**: Any special instructions
   - **Reminder Information**: Important details to remember

#### Medical Statistics

1. **Appointment Metrics**
   - **Total Appointments**: Lifetime appointment count
   - **Upcoming Count**: Number of scheduled future appointments
   - **Recent Activity**: Appointments in last 30 days

2. **Health Overview**
   - **Conditions Tracked**: Number of diagnosed conditions
   - **Treatment Duration**: Length of ongoing treatments
   - **Recovery Progress**: Improvement tracking

### Account Management

#### Updating Profile

Currently, profile updates require administrator assistance. Contact hospital staff for:
- **Address Changes**: Moving to new residence
- **Phone Updates**: New contact number
- **Email Changes**: Updated email address
- **Emergency Contacts**: Adding or updating emergency contacts

#### Password Security

- **Strong Passwords**: Use combination of letters, numbers, symbols
- **Regular Updates**: Change password periodically
- **Keep Secure**: Don't share login credentials
- **Logout Properly**: Always logout when finished

## Doctor Portal

### Login Process

#### Doctor Authentication

1. **Enter Credentials**
   - Doctor ID: Your assigned 4-digit ID
   - Password: Your secure password

2. **Session Management**
   - System tracks your login time
   - Maintains session for 24 hours
   - Automatic logout for security

### Dashboard Overview

#### Welcome Section
- **Personalized Greeting**: Welcome message with your name
- **Current Session**: Login time and session status
- **Quick Statistics**: Overview of your current workload

#### Appointment Queue

1. **Current Queue Display**
   - **Patient List**: Patients waiting for appointments
   - **Appointment Times**: Scheduled consultation times
   - **Priority Order**: First-come, first-served basis
   - **Patient Details**: ID, age, chief complaint

2. **Queue Statistics**
   - **Total Scheduled**: Number of appointments today
   - **Remaining**: Appointments still to process
   - **Average Time**: Estimated time per consultation

#### Next Appointment Preview

- **Patient Information**: Next patient's details
- **Appointment Time**: Scheduled consultation time
- **Chief Complaint**: Reason for visit
- **Medical History**: Relevant background information

### Processing Appointments

#### Appointment Workflow

1. **Review Next Patient**
   - Click "Process Next" to view patient details
   - Review patient's medical history
   - Note chief complaint and symptoms

2. **Patient Information Screen**
   - **Demographics**: Patient age, address, contact info
   - **Medical History**: Previous conditions and treatments
   - **Current Complaint**: Reason for today's visit
   - **Vital Signs**: If available from preliminary screening

#### Consultation Documentation

1. **Medical Notes**
   - **Assessment**: Your medical evaluation
   - **Diagnosis**: Identified conditions or concerns
   - **Treatment Plan**: Prescribed medications, procedures, or referrals
   - **Follow-up**: Any required return visits

2. **Updating Patient Records**
   - Add new diagnoses to patient's medical history
   - Update treatment status
   - Note any changes in condition
   - Record prescribed medications

#### Completing Consultations

1. **Finish Appointment**
   - Save all medical notes
   - Update patient's record
   - Mark appointment as completed
   - Move to next patient in queue

2. **Treatment Instructions**
   - Provide clear instructions to patient
   - Explain medication dosages
   - Schedule follow-up if necessary
   - Give patient copy of visit summary

### Patient Directory

#### Accessing Patient Information

1. **Open Patient Directory**
   - Click "View Patients" from dashboard
   - Browse complete patient database
   - Search for specific patients

2. **Search Functionality**
   - **By Patient ID**: Enter specific patient ID
   - **By Name**: Search using patient name
   - **By Condition**: Find patients with specific conditions

#### Patient Details

1. **Contact Information**
   - **Name and ID**: Patient identification
   - **Address**: Current residence
   - **Phone**: Contact number
   - **Email**: Electronic contact

2. **Medical Overview**
   - **Age and Demographics**: Basic information
   - **Medical History**: Past conditions and treatments
   - **Current Medications**: Ongoing prescriptions
   - **Allergies**: Known allergic reactions

### Session Management

#### Login Tracking

- **Automatic Recording**: System logs all login times
- **Session Duration**: Tracks time spent logged in
- **Activity Monitoring**: Records consultation activities

#### Logout Process

1. **Proper Logout**
   - Use "Logout" button to end session safely
   - System updates your status to offline
   - Session time is recorded in your history

2. **Automatic Logout**
   - Sessions expire after 24 hours of inactivity
   - System will prompt for re-authentication
   - No data is lost during automatic logout

## Admin Panel

### Administrator Login

#### Access Requirements

1. **Admin Credentials**
   - Username: Your assigned admin username
   - Password: Secure admin password
   - Super Admin privileges for advanced functions

2. **Security Features**
   - Enhanced authentication for admin accounts
   - Activity logging for all admin actions
   - Session timeouts for security

### Dashboard Overview

#### System Statistics

1. **User Metrics**
   - **Total Doctors**: Number of registered doctors
   - **Active Doctors**: Currently logged-in doctors
   - **Total Patients**: Number of registered patients
   - **New Registrations**: Recent sign-ups

2. **Appointment Statistics**
   - **Total Appointments**: All scheduled appointments
   - **Today's Appointments**: Current day's schedule
   - **Completed**: Finished consultations
   - **Pending**: Awaiting appointments

#### System Health

- **Data Integrity**: System data validation status
- **Performance Metrics**: System response times
- **Error Logs**: Any system errors or warnings
- **Backup Status**: Data backup verification

### Doctor Management

#### Viewing All Doctors

1. **Doctor Directory**
   - **Complete List**: All registered doctors
   - **Personal Information**: Name, ID, contact details
   - **Professional Details**: Specialty, credentials
   - **Status**: Online/offline, login history

2. **Active Sessions**
   - **Currently Logged In**: Doctors currently online
   - **Session Duration**: How long they've been logged in
   - **Activity Status**: Current consultation status

#### Doctor Registration Approval

1. **Pending Registrations**
   - **Application Queue**: Doctors awaiting approval
   - **Application Details**: Credentials, specialty, contact info
   - **Review Process**: Verification of medical licenses

2. **Approval Process**
   - **Review Application**: Check credentials and information
   - **Approve Registration**: Accept new doctor into system
   - **Assign Doctor ID**: Generate unique identification
   - **Send Confirmation**: Notify doctor of approval

3. **Rejection Process**
   - **Decline Application**: Reject incomplete or invalid applications
   - **Provide Reason**: Explain rejection to applicant
   - **Request Resubmission**: Allow for corrected applications

#### Doctor Account Management

1. **Update Doctor Information**
   - **Personal Details**: Name, contact information
   - **Professional Info**: Specialty, credentials
   - **System Access**: Login privileges, system permissions

2. **Remove Doctor Accounts**
   - **Account Deletion**: Remove doctors from system
   - **Data Preservation**: Maintain appointment history
   - **Notification**: Inform doctor of account closure

### Patient Management

#### Patient Directory

1. **Complete Patient List**
   - **All Registered Patients**: Every patient in system
   - **Personal Information**: Demographics, contact details
   - **Medical Summary**: Current conditions, treatment status
   - **Account Status**: Active, inactive, suspended

2. **Advanced Search Features**
   - **Search by ID**: Find specific patient by ID number
   - **Search by Name**: Locate patients by name
   - **Filter Options**: Age range, condition, registration date

#### Patient Account Management

1. **View Patient Details**
   - **Complete Profile**: All patient information
   - **Medical History**: Past conditions and treatments
   - **Appointment History**: All past and future appointments
   - **Account Activity**: Login history, system usage

2. **Update Patient Information**
   - **Correct Errors**: Fix incorrect information
   - **Update Contact Info**: New address, phone, email
   - **Medical Updates**: Add conditions, update status

3. **Remove Patient Accounts**
   - **Account Deletion**: Remove patients from system
   - **Data Archival**: Preserve medical history for legal requirements
   - **Notification**: Inform patient of account closure

### Appointment Oversight

#### System-Wide Appointments

1. **All Appointments View**
   - **Complete Schedule**: Every appointment in system
   - **Status Tracking**: Scheduled, completed, cancelled
   - **Doctor Assignment**: Which doctor for each appointment
   - **Patient Information**: Patient details for each appointment

2. **Appointment Analytics**
   - **Busy Periods**: Peak appointment times
   - **Doctor Workload**: Appointments per doctor
   - **Patient Patterns**: Frequent visitors, long gaps
   - **System Efficiency**: Average consultation times

#### Managing Appointments

1. **Appointment Modification**
   - **Reschedule**: Change appointment times
   - **Reassign Doctor**: Move to different doctor
   - **Cancel Appointments**: Remove from schedule
   - **Add Emergency Slots**: Insert urgent appointments

2. **Conflict Resolution**
   - **Double Bookings**: Resolve scheduling conflicts
   - **Doctor Availability**: Handle doctor absences
   - **Patient Rescheduling**: Accommodate patient needs

### System Administration

#### User Account Oversight

1. **Account Security**
   - **Password Policies**: Enforce strong passwords
   - **Account Lockouts**: Handle failed login attempts
   - **Session Management**: Monitor active sessions
   - **Suspicious Activity**: Identify unusual behavior

2. **System Maintenance**
   - **Data Backup**: Regular data preservation
   - **System Updates**: Software maintenance
   - **Performance Monitoring**: System speed and reliability
   - **Error Resolution**: Fix system issues

#### Reports and Analytics

1. **Usage Statistics**
   - **System Usage**: How often system is used
   - **User Activity**: Most active users and features
   - **Growth Metrics**: New registrations over time
   - **Performance Trends**: System performance over time

2. **Medical Statistics**
   - **Common Conditions**: Most frequent patient complaints
   - **Treatment Effectiveness**: Recovery rates and times
   - **Doctor Performance**: Consultation efficiency
   - **Patient Satisfaction**: Feedback and ratings

## Common Tasks

### Quick Reference Guide

#### For Patients

**Booking an Appointment:**
1. Login → Dashboard → "Book Appointment"
2. Select available doctor
3. Choose date and time
4. Describe medical concern
5. Confirm booking

**Viewing Medical Records:**
1. Login → Dashboard → "Medical Records"
2. Review personal information
3. Check medical history
4. View appointment history

**Updating Account:**
1. Contact hospital staff for profile changes
2. Change password regularly for security
3. Keep contact information current

#### For Doctors

**Processing Appointments:**
1. Login → Dashboard → "Process Next"
2. Review patient information
3. Conduct consultation
4. Update medical notes
5. Complete appointment

**Viewing Patient Directory:**
1. Dashboard → "View Patients"
2. Search for specific patients
3. Review patient medical history
4. Access contact information

**Managing Sessions:**
1. Login properly at start of shift
2. Stay logged in during work hours
3. Logout properly at end of shift

#### For Administrators

**Approving Doctor Registrations:**
1. Admin Panel → "Doctor Management"
2. Review pending registrations
3. Verify credentials
4. Approve or reject applications

**Managing User Accounts:**
1. Navigate to user management section
2. Search for specific users
3. Update user information
4. Remove accounts if necessary

**System Monitoring:**
1. Check system statistics regularly
2. Review error logs
3. Monitor user activity
4. Ensure data integrity

### Best Practices

#### Security Guidelines

1. **Password Management**
   - Use strong, unique passwords
   - Change passwords regularly
   - Don't share login credentials
   - Report suspicious activity

2. **Session Security**
   - Always logout when finished
   - Don't leave system unattended while logged in
   - Use system on secure networks only
   - Report unauthorized access attempts

3. **Data Protection**
   - Keep patient information confidential
   - Don't access records unnecessarily
   - Report data breaches immediately
   - Follow hospital privacy policies

#### Efficiency Tips

1. **For Doctors**
   - Review patient history before consultations
   - Keep detailed medical notes
   - Update patient records promptly
   - Use patient directory for quick lookups

2. **For Patients**
   - Book appointments in advance
   - Prepare symptom descriptions beforehand
   - Keep track of appointment IDs
   - Update contact information regularly

3. **For Administrators**
   - Monitor system performance regularly
   - Process doctor registrations promptly
   - Maintain accurate user records
   - Generate reports for insights

## Troubleshooting

### Common Issues and Solutions

#### Login Problems

**Issue**: Cannot login with correct credentials
**Solutions**:
1. Check username/password spelling
2. Ensure Caps Lock is off
3. Clear browser cache if using web version
4. Contact administrator for account verification

**Issue**: Session expires too quickly
**Solutions**:
1. Check system date/time settings
2. Avoid long periods of inactivity
3. Contact administrator for session settings
4. Re-login to start fresh session

#### Appointment Booking Issues

**Issue**: No doctors available for booking
**Solutions**:
1. Check if doctors are currently logged in
2. Try different time slots
3. Contact hospital for doctor schedules
4. Book appointment for future date

**Issue**: Cannot select desired date/time
**Solutions**:
1. Ensure date is in the future
2. Check hospital operating hours
3. Verify time format (HH:mm)
4. Choose different time slot

#### Data Display Problems

**Issue**: Information not displaying correctly
**Solutions**:
1. Refresh the page/restart application
2. Check internet connection
3. Verify data file integrity
4. Contact technical support

**Issue**: Missing appointment or patient data
**Solutions**:
1. Check if data was saved properly
2. Verify correct patient/doctor ID
3. Check system date/time settings
4. Contact administrator for data recovery

#### Performance Issues

**Issue**: System running slowly
**Solutions**:
1. Close other applications
2. Restart the application
3. Check available disk space
4. Contact technical support

**Issue**: System freezing or crashing
**Solutions**:
1. Force close and restart application
2. Check system requirements
3. Verify data file integrity
4. Report issue to technical support

### Error Messages

#### Common Error Messages and Meanings

**"Patient not found"**
- Patient ID doesn't exist in system
- Check ID number spelling
- Contact administrator to verify registration

**"Session expired"**
- User session has timed out
- Re-login to continue
- System automatically logs out after 24 hours

**"Invalid credentials"**
- Username or password is incorrect
- Check spelling and case sensitivity
- Contact administrator for password reset

**"Access denied"**
- User doesn't have permission for requested action
- Check user role and permissions
- Contact administrator for access rights

**"Data file error"**
- System cannot read/write data files
- Check file permissions
- Contact technical support for file recovery

### Getting Help

#### Support Channels

1. **Technical Support**
   - Email: support@hospital.com
   - Phone: Hospital main number
   - In-person: IT Department

2. **Account Issues**
   - Contact hospital administration
   - Visit registration desk
   - Email: admin@hospital.com

3. **Medical Questions**
   - Contact your assigned doctor
   - Call hospital main number
   - Visit emergency department for urgent issues

4. **System Training**
   - Request training session
   - Review user documentation
   - Contact system administrator

## FAQ

### Frequently Asked Questions

#### General System Questions

**Q: How secure is my personal information?**
A: The system uses secure authentication and data encryption. All medical information is kept confidential according to hospital privacy policies.

**Q: Can I access the system from home?**
A: Currently, the system is designed for use within the hospital network only. Remote access may be available for authorized personnel.

**Q: What happens if I forget my password?**
A: Contact the hospital administrator or IT department for password reset assistance.

**Q: How long are my records kept in the system?**
A: Medical records are maintained according to legal requirements and hospital policies. Contact administration for specific retention policies.

#### Patient-Specific Questions

**Q: Can I book multiple appointments at once?**
A: Currently, you can book one appointment at a time. For multiple appointments, repeat the booking process.

**Q: Can I cancel or reschedule my appointment?**
A: Contact the hospital directly to cancel or reschedule appointments. The system currently doesn't support patient-initiated changes.

**Q: Why can't I see all doctors in the booking system?**
A: Only doctors who are currently logged into the system appear in the booking list. This ensures they're available for appointments.

**Q: How do I update my contact information?**
A: Contact hospital administration to update your personal information. Direct patient updates are not currently supported.

#### Doctor-Specific Questions

**Q: How do I handle emergency appointments?**
A: Contact administration to add emergency slots to your schedule. The system supports priority appointment insertion.

**Q: Can I view my schedule for future dates?**
A: The current system focuses on today's appointments. Contact administration for extended schedule viewing.

**Q: What if I can't complete all my appointments?**
A: Notify administration immediately. They can help reschedule patients or arrange coverage.

**Q: How do I update patient medical records?**
A: Use the appointment processing interface to add notes and update patient conditions during consultations.

#### Administrator Questions

**Q: How do I backup system data?**
A: Contact technical support for data backup procedures. Regular backups are essential for data protection.

**Q: Can I generate custom reports?**
A: The system provides standard reports. Contact technical support for custom reporting requirements.

**Q: How do I handle system errors?**
A: Document the error details and contact technical support immediately. Keep error logs for troubleshooting.

**Q: What's the maximum number of users the system supports?**
A: The system is designed to handle typical hospital volumes. Contact technical support for capacity planning.

---

This completes the comprehensive User Guide for the Hospital Management System. For additional assistance, please contact your system administrator or technical support team.
