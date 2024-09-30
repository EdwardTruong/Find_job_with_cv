$(document).ready(function() {
    $('#passwordInput, #confirmPasswordInput').on('keyup', function(e) {    
        if($('#passwordInput').val() == '' && $('#confirmPasswordInput').val() == '')
        {
            $('#passwordStrength').removeClass().html('');
            return false;
        }
     if($('#passwordInput').val() != '' && $('#confirmPasswordInput').val() != '' && $('#passwordInput').val() != $('#confirmPasswordInput').val())
        {
            $('#passwordStrength').removeClass().addClass('alert alert-danger').html('Mật khẩu không khớp!');
            return false;
        }
        // Must have capital letter, numbers and lowercase letters
        var strongRegex = new RegExp("^(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*\\W).*$", "g");
        // Must have either capitals and lowercase letters or lowercase and numbers
        var mediumRegex = new RegExp("^(?=.{7,})(((?=.*[A-Z])(?=.*[a-z]))|((?=.*[A-Z])(?=.*[0-9]))|((?=.*[a-z])(?=.*[0-9]))).*$", "g");
        // Must be at least 6 characters long
        var okRegex = new RegExp("(?=.{5,}).*", "g");
        if (okRegex.test($(this).val()) === false) {
            // If ok regex doesn't match the password
            $('#passwordStrength').removeClass().addClass('alert alert-danger').html('Mật khẩu phải có ít nhất 5 ký tự.');
        } else if (strongRegex.test($(this).val())) {
            // If reg ex matches strong password
            $('#passwordStrength').removeClass().addClass('alert alert-success').html('Good Password !');
        } else if (mediumRegex.test($(this).val())) {
            // If medium password matches the reg ex
            $('#passwordStrength').removeClass().addClass('alert alert-warning').html('Hãy thêm ít nhất 1 ký tự hoa hoặc một ký tự thường hoặc một chữ số hoặc một ký tự đặc biệt !  Ví dụ : Test123@');
        } else {
            // If password is ok
            $('#passwordStrength').removeClass().addClass('alert alert-warning').html('Mật khẩu yếu. Hãy sử dụng thêm vài số và vài ký tự hoa ! Ví dụ : Test123@');
        }
        return true;
    });
    });