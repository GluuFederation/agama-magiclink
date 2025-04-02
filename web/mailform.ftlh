[#ftl output_format="HTML"]
<!DOCTYPE html>
<html lang="en">
<head><meta charset="UTF-8"><meta http-equiv="X-UA-Compatible" content="IE=edge"><meta name="viewport" content="width=device-width, initial-scale=1.0"><title>Mail Form
	</title><link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/tailwindcss@2.2/dist/tailwind.min.css" type="text/css"></head>
<body>

  
	
	
	
	
	
  
  
	<div class="md:h-screen bg-white relative flex flex-col justify-center items-center">
	  <div class="md:border md:border-gray-300 bg-white md:shadow-lg shadow-none rounded p-10 align-middle">
		<div class="flex flex-col items-center space-y-3">
		  <div class="flex">
			<img src="https://gluu.org/wp-content/uploads/2020/12/logo.png">
		  </div>
		  <span class="text-2xl font-semi-bold leading-normal">Form</span>
		  <p class="leading-normal">Enter your E-mail
		  </p>
		</div>
		[#if errorMessage?has_content]
		<div id="ig86l" align="center" class="fs-6 text-red-600 mb-4">${errorMessage}
		</div>
		[/#if]
		<form method="post" class="my-8">
		  <div class="relative mb-2">
			<label for="mail" class="label mt-2 ml-1 leading-tighter text-gray-600 text-base cursor-text">
			  E-mail
			</label>
			<input type="text" id="mail" name="mail" autofocus="" required="" class="w-full rounded px-3 border border-gray-300 pt-2 pb-2">
		  </div>
		  <div class="space-y-9">
			<div class="text-sm flex justify-center items-center">
			  <button type="submit" class="py-2 px-6 rounded text-white btn bg-green-500 hover:bg-green-600">
				Submit
			  </button>
			</div>
		  </div>
		</form>
	  </div>
	</div>
  
</body>


</html>