package it.chalmers.gamma.domain.activationcode.service;

import it.chalmers.gamma.util.domain.Cid;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ActivationCodeFinderTest {

    @Mock
    private ActivationCodeRepository activationCodeRepository;

    @InjectMocks
    private ActivationCodeFinder activationCodeFinder;

    @Test
    void getAll() {
        List<ActivationCode> activationCodeList = new ArrayList<>();
        activationCodeList.add(ActivationCodeFactory.create(new Cid("mycid"), Code.generate()));
        activationCodeList.add(ActivationCodeFactory.create(new Cid("mycidi"), Code.generate()));

        //given
        given(activationCodeRepository.findAll())
                .willReturn(activationCodeList);

        //when
        List<ActivationCodeDTO> activationCodeDTOList = activationCodeFinder.getAll();
        for (int i = 0; i < activationCodeDTOList.size(); i++) {
            ActivationCode ac = activationCodeList.get(0);
            ActivationCodeDTO acDTO = activationCodeDTOList.get(0);

            assertThat(ac)
                    .usingRecursiveComparison()
                    .isEqualTo(acDTO);
        }
    }

    @Test
    void codeMatchesCid() {
        Cid cid = new Cid("mycid");
        Code code = Code.generate();

        //given
        given(activationCodeRepository.findActivationCodeByCidAndCode(any(Cid.class), any(Code.class)))
                .willReturn(Optional.empty());
        given(activationCodeRepository.findActivationCodeByCidAndCode(eq(cid), eq(code)))
                .willReturn(Optional.of(ActivationCodeFactory.create(cid, code)));

        //when
        boolean combination1 = activationCodeFinder.codeMatchesCid(cid, code);
        boolean combination2 = activationCodeFinder.codeMatchesCid(cid, Code.generate());

        //then
        assertThat(combination1).isTrue();
        assertThat(combination2).isFalse();
    }
}