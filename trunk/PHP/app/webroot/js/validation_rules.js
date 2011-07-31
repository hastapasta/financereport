function validate(ob){
    var canSubmit = true;
    if(ob == null){
    	ob = "";
    }else{
    	ob = "#" + $(ob).attr("id")+" ";
    }
    
    $('form').find(".error-message").remove();
    $( ob + ".numeric").each(function(){
        var rx = /^([])|([0-9]{1,11})$/;
        if(!rx.test($(this).val()) && (!$(this).val()=="")){
            $(this).parent().find(".error-message").remove();
            $(this).after("<div class='error-message'>Must be numeric</div>")
            canSubmit=false;
        }
    });
    $(ob + '.validEmail').each(function(){
        var filter =/^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\.([a-zA-Z])+([a-zA-Z])+/;
        if(!filter.test( $(this).val())){
            $(this).parent().find(".error-message").remove();
            $(this).after("<div class='error-message'>Please provide a valid email address</div>");
            canSubmit=false;
        }
    })
    $(ob + ".notEmpty").each(function(){
        if($(this).val()==""){
            $(this).parent().find(".error-message").remove();
            $(this).after("<div class='error-message'>Value required</div>");
            canSubmit=false;
        }
    });
    return canSubmit;
}

