<?php

require_once 'config.php';

// get parameters
if(isset($_REQUEST['name']) && isset($_REQUEST['father_name']) && isset($_REQUEST['teacher_id'])) {

    $name = $_REQUEST['name'];
    $father_name = $_REQUEST['father_name'];
    $teacher_id = $_REQUEST['teacher_id'];
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

$sqlquery = "INSERT INTO `Student` (`teacher_id`, `name`, `father_name`) VALUES ($teacher_id, '$name', '$father_name')";

if ($con->query($sqlquery) === true){
    $content = mysqli_query($con , "SELECT `id` FROM `Student` WHERE `name`='$name' AND `father_name`='$father_name' AND `teacher_id`=$teacher_id");
    $output = array();

    while($row = mysqli_fetch_array($content)){
        $record  = array();
        $record['id'] = $row['id'];
        $output[] = $record;
    }
    echo json_encode($output);
} else{
    echo "Error : " . $con->error;
}
