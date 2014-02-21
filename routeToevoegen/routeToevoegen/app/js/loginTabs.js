window.onload = function () {
    var login = document.getElementById('login');
    var register = document.getElementById('register');
    var password = document.getElementById('password');
    document.getElementById('loginIcon').addEventListener('click', function () {
        login.style.display = 'block';
        register.style.display = 'none';
        password.style.display = 'none';
    });
    document.getElementById('registerIcon').addEventListener('click', function () {
        register.style.display = 'block';
        password.style.display = 'none';
        login.style.display = 'none';
    });
    document.getElementById('passwordIcon').addEventListener('click', function () {
        password.style.display = 'block';
        register.style.display = 'none';
        login.style.display = 'none';
    });
}


