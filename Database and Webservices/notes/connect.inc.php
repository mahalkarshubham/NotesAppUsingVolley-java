<?php
$server = 'localhost';
$username = 'root';
$password = '';
$database = 'notes';
date_default_timezone_set('Asia/Kolkata');
$timestamp = date('Y-m-d');
$timestamp_dmy = date('d-m-Y');

try{
	$connect = new PDO("mysql:host=$server;dbname=$database;charset=utf8", $username, $password);
    @session_start();
} catch(PDOException $e){
	die( "Connection failed: " . $e->getMessage());
}
?>