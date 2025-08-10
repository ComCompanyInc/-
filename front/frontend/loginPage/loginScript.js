document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    // Теперь $.ajax будет работать, так как jQuery подключен
    $.ajax({
        url: 'http://localhost:7070/login',
        method: 'POST',
        data: { 
            login: username, 
            password: password 
        },
        success: function(data) {
            const responseContainer = document.getElementById('response');
            responseContainer.innerHTML = `
                <h3>Ответ сервера:</h3>
                <pre>${JSON.stringify(data, null, 2)}</pre>
            `;
            responseContainer.style.display = 'block';
        },
        error: function(xhr) {
            const responseContainer = document.getElementById('response');
            responseContainer.innerHTML = `
                <p style="color: red;">Ошибка: ${xhr.status} ${xhr.statusText}</p>
            `;
            responseContainer.style.display = 'block';
        }
    });
});

