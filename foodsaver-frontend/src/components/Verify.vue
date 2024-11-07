<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Email Verification</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 20px;
      text-align: center;
    }
    h1 {
      color: #4CAF50;
    }
    .message {
      padding: 10px;
      margin: 20px 0;
      border-radius: 5px;
    }
    .success {
      background-color: #d4edda;
      color: #155724;
    }
    .error {
      background-color: #f8d7da;
      color: #721c24;
    }
    .info {
      background-color: #cce5ff;
      color: #004085;
    }
    button {
      padding: 10px 20px;
      background-color: #007bff;
      color: white;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }
    button:hover {
      background-color: #0056b3;
    }
  </style>
</head>
<body>

<h1>Email Verification</h1>

<div id="message" class="message"></div>

<script>
  // Hämta URL-parametern 'status'
  const urlParams = new URLSearchParams(window.location.search);
  const status = urlParams.get('status');
  const messageDiv = document.getElementById('message');

  // Beroende på status, visa lämpligt meddelande
  if (status === 'success') {
    messageDiv.classList.add('success');
    messageDiv.innerHTML = '<p>Congratulations, your email has been successfully verified!</p>';
  } else if (status === 'expired') {
    messageDiv.classList.add('error');
    messageDiv.innerHTML = '<p>The verification link has expired. Please request a new link.</p>';
    messageDiv.innerHTML += '<button onclick="resendVerificationLink()">Resend Link</button>';
  } else if (status === 'resend') {
    messageDiv.classList.add('info');
    messageDiv.innerHTML = '<p>A new verification link has been sent to your email address.</p>';
  } else if (status === 'error') {
    messageDiv.classList.add('error');
    messageDiv.innerHTML = '<p>There was an error during the verification process. Please try again.</p>';
  } else {
    messageDiv.classList.add('info');
    messageDiv.innerHTML = '<p>Waiting for your verification...</p>';
  }

  // Funktion för att begära en ny verifieringslänk
  function resendVerificationLink() {
    const email = prompt("Please enter your email address:");
    if (email) {
      fetch(`/api/auth/resend-verification?email=${email}`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Error sending the verification link.');
            }
            return response.json();
          })
          .then(data => {
            alert('A new verification link has been sent to your email address!');
          })
          .catch(error => {
            alert('Error: ' + error.message);
          });
    }
  }
</script>

</body>
</html>
