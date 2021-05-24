<?php

require_once 'config.php';
// get parameters
if (isset($_REQUEST['id'])){
    $id = $_REQUEST['id'];
} else{
    return;
}

// connect to database
$con = new mysqli(DB_HOST , DB_USER , DB_PASS , DB_NAME);

// check connection
if ($con->connect_error) {
    echo "Failed to connect : " . $con->connect_error;
    return;
}
mysqli_query( $con , "SET CHARACTER SET utf8;" );

$content = mysqli_query($con , "SELECT Student.name, `father_name`, Student.id, Teacher.name AS `tname`, Teacher.id AS `tid` FROM `Student` JOIN `Teacher` ON Student.teacher_id=Teacher.id WHERE Student.id=$id");
$output = array();

while($row = mysqli_fetch_array($content)){
    $record  = array();
    $record['name'] = $row['name'];
    $record['tname'] = $row['tname'];
    $record['father_name'] = $row['father_name'];
    $record['id'] = $row['id'];
    $record['tid'] = $row['tid'];
    $output[] = $record;
}

echo json_encode($output);
