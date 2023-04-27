window.addEventListener('load', function () {
    emailInput.focus();
});

///////////////////////////////////

const submitButton = document.getElementById('join-btn');

submitButton.addEventListener('click', function (e) {
    const email = emailInput.value.trim();
    const password = passwordInput.value.trim();
    const nickname = nicknameInput.value.trim();
    if (email === '' || !emailRegex.test(email)) {
        e.preventDefault();
        validateEmail();
    } else if (password === '' || !passwordRegex.test(password)) {
        e.preventDefault();
        validatePassword();
    } else if (nickname === '' || !nicknameRegex.test(nickname)) {
        e.preventDefault();
        validateNickname();
    }
});

///////////////////////////////////

const emailInput = document.getElementById('email');
const emailErrorMessageByServer = document.getElementById('email-error-by-server');
const emailErrorMessage = document.getElementById('email-error');
const emailRegex = /[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]+$/;

emailInput.addEventListener('focusin', function (e) {
    if (emailErrorMessageByServer != null) {
        emailErrorMessageByServer.style.display = 'none';
    }

    validateEmail();
});

emailInput.addEventListener('blur', function (e) {
    validateEmail();
});

emailInput.addEventListener('input', function (e) {
    validateEmail();
});

function validateEmail() {
    const email = emailInput.value.trim();
    if (email === '') {
        setError(emailInput, emailErrorMessage, '이메일은 필수 입력 항목입니다.');
        emailInput.focus();
    } else if (!emailRegex.test(email)) {
        setError(emailInput, emailErrorMessage, '이메일 형식이 올바르지 않습니다.');
        emailInput.focus();
    } else {
        clearError(emailInput, emailErrorMessage);
    }
}

///////////////////////////////////

const passwordInput = document.getElementById('password');
const passwordErrorMessageByServer = document.getElementById('password-error-by-server');
const passwordErrorMessage = document.getElementById('password-error');
const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,20}$/;

passwordInput.addEventListener('focusin', function (e) {
    if (passwordErrorMessageByServer != null) {
        passwordErrorMessageByServer.style.display = 'none';
    }

    validatePassword();
});

passwordInput.addEventListener('blur', function (e) {
    validatePassword();
});

passwordInput.addEventListener('input', function (e) {
    validatePassword();
});

function validatePassword() {
    const password = passwordInput.value.trim();
    if (password === '') {
        setError(passwordInput, passwordErrorMessage, '비밀번호는 필수 입력 항목입니다.');
        passwordInput.focus();
    } else if (password.length < 8 || password.length > 20) {
        setError(passwordInput, passwordErrorMessage, '비밀번호는 8장 이상, 20자 이하여야 합니다.');
        passwordInput.focus();
    } else if (!passwordRegex.test(password)) {
        setError(passwordInput, passwordErrorMessage, '비밀번호는 영문, 숫자, 특수문자를 모두 포함해야 합니다.');
        passwordInput.focus();
    } else {
        clearError(passwordInput, passwordErrorMessage);
    }
}

///////////////////////////////////

const nicknameInput = document.getElementById('nickname');
const nicknameErrorMessageByServer = document.getElementById('nickname-error-by-server');
const nicknameErrorMessage = document.getElementById('nickname-error');
const nicknameRegex = /^[a-zA-Z0-9_.]{5,20}$/;

nicknameInput.addEventListener('focusin', function (e) {
    if (nicknameErrorMessageByServer != null) {
        nicknameErrorMessageByServer.style.display = 'none';
    }

    validateNickname();
});

nicknameInput.addEventListener('blur', function (e) {
    validateNickname();
});

nicknameInput.addEventListener('input', function (e) {
    validateNickname();
});

function validateNickname() {
    const nickname = nicknameInput.value.trim();
    if (nickname === '') {
        setError(nicknameInput, nicknameErrorMessage, '닉네임은 필수 입력 항목입니다.');
        nicknameInput.focus();
    } else if (nickname.length < 5 || nickname.length > 20) {
        setError(nicknameInput, nicknameErrorMessage, '닉네임은 5자 이상, 20자 이하여야 합니다.');
        nicknameInput.focus();
    } else if (!nicknameRegex.test(nickname)) {
        setError(nicknameInput, nicknameErrorMessage, '닉네임은 영문, 숫자, 특수문자 \'_\' 와 \'.\' 만 사용 가능합니다.');
        nicknameInput.focus();
    } else {
        clearError(nicknameInput, nicknameErrorMessage);
    }
}

///////////////////////////////////

function setError(inputElement, errorMessageElement, message) {
    inputElement.classList.add('is-invalid');
    errorMessageElement.style.display = 'inline';
    errorMessageElement.textContent = message;
}

function clearError(inputElement, errorMessageElement) {
    inputElement.classList.remove('is-invalid');
    inputElement.classList.add('is-valid');
    errorMessageElement.style.display = 'inline';
    errorMessageElement.style.display = 'none';
}
