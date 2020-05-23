<?php
require 'connect.inc.php';
$response = array();

@header('Content-Type: application/json; charset=utf-8'); 

if(
 isset($_POST['notes_title'])&&
 isset($_POST['notes_description'])){

 $notes_title = @$_POST['notes_title'];
 $notes_description = @$_POST['notes_description'];

 if(
  !empty($notes_title)&&
  !empty($notes_description)){

  $status = 'Active';
  $timestamp = date('Y-m-d');

  $query = "INSERT
    INTO
        `notes`(
        `notes_title`,
        `notes_description`,
        `status`,
        `timestamp`
      )
    VALUES(
      :notes_title,
      :notes_description,
      :status,
      :timestamp
    )";
  $statment = $connect->prepare($query);
  $result = $statment->execute(
   array(
    ":notes_title" => $notes_title,
    ":notes_description" => $notes_description,
    ":status" => "Active",
    ":timestamp" => $timestamp,
   ));

  if($result){

   $notes_id = $connect->lastInsertId();

   $response [0]['status'] = "Active";
   $response [0]['message'] = "User registered succesfully";
   $response [0]['notes_id'] = $notes_id;
   $response [0]['notes_title'] = $notes_title;
   $response [0]['notes_description'] = $notes_description;

   echo json_encode($response);

  }else{
   $response [0]['status'] = "Failed";
   $response [0]['message'] = "Failed to register, try again";
   echo json_encode($response);
  }
 }else{
  $response [0]['status'] = "Failed";
  $response [0]['message'] = "* is mandatory";
  echo json_encode($response);
 }
}else{
 $response [0]['status'] = "Failed";
 $response [0]['message'] = "Some parameters are missing, try again";
 echo json_encode($response);
}
?>