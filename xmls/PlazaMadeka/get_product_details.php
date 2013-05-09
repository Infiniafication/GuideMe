<?php 
	require_once __DIR__ . '/db_config.php';

	$response = array();

	$db = new DB_CONNECT();

	if(isset($_GET["pid"])){
		$pid = $_GET['pid'];

		//geting products from product table
		$result = mysql_query("SELECT *FROM products WHERE pid = $pid");

		if(!empty($result)){
			if(mysql_num_rows($result) > 0){
				$result = mysql_fetch_array($result);

				$product = array();

				$product["pid"] = $result["pid"];
				$product["name"] = $result["name"];
				$product["price"] = $result["price"];
				$product["description"] = $result["description"];
				$product["created_at"] = $result["created_at"];
				$product["updated_at"] = $result["updated_at"];

				$response["success"] = 1;
				$response["product"] = array();

				array_push($response["product"], $product);

				echo json_encode($response);


			} else {
				$response["success"] = 0;
				$response["message"] = "No product found";

				echo json_encode($response);
			}
		} else{
			$response["success"] = 0;
			$response["message"] = "No products found";

			echo json_encode($response);
		}

	}else{
		$response["success"] = 0;
		$response["message"] = "Required filed(s) is missing";

		echo json_encode($response);
	}





 ?>