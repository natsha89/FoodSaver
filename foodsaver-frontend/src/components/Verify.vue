<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Email Verification</title>
</head>
<body>
<h1>Email Verification</h1>

<div id="message"></div>

<script>
  // Hämta URL-parametern 'status'
  const urlParams = new URLSearchParams(window.location.search);
  const status = urlParams.get('status');
  const messageDiv = document.getElementById('message');

  if (status === 'success') {
    messageDiv.innerHTML = '<p>Congratulations, your email has been successfully verified!</p>';
  } else if (status === 'expired') {
    messageDiv.innerHTML = '<p>The verification link has expired. Please request a new link.</p>';
    messageDiv.innerHTML += '<button onclick="resendVerificationLink()">Resend Link</button>';
  } else if (status === 'resend') {
    messageDiv.innerHTML = '<p>A new verification link has been sent to your email address.</p>';
  } else if (status === 'error') {
    messageDiv.innerHTML = '<p>There was an error during the verification process. Please try again.</p>';
  }

  function resendVerificationLink() {
    const email = prompt("Please enter your email address:");
    if (email) {
      fetch(`/api/auth/resend-verification?email=${email}`)
          .then(response => response.json())
          .then(data => {
            // Här kan vi visa ett meddelande om att länken skickades
            alert('A new verification link has been sent!');
          })
          .catch(error => {
            alert('Error sending the verification link. Please try again.');
          });
    }
  }
</script>
</body>
</html>
