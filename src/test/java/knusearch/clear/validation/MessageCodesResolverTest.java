package knusearch.clear.validation;

import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageCodesResolverTest { //errors.properties를 사용하기 위해,
    //BindingResult의 reject, rejectValue가 생성해내는 "메세지 코드"를 테스트함

    MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");

        //assertThat(A).containsExactly(~~) : A에 들어있는 것이 ~~와 정확히 일치해야 한다. (순서도 같아야 함)
        assertThat(messageCodes).containsExactly("required.item", "required");
    }

    @Test
    void messageCodesResolverField() {
        String[] messageCodes= codesResolver.resolveMessageCodes("required","item"
                ,"itemName", List.class);

        assertThat(messageCodes).containsExactly( "required.item.itemName"
                ,"required.itemName"
                ,"required.java.util.List"
                ,"required");
    }


}
