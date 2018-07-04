import React, { Component } from "react";
import PropTypes from "prop-types";
import * as yup from "yup";

import { CIDInput } from "./InputCid.view.styles";

import GammaButton from "../../../../common/elements/gamma-button";
import GammaForm from "../../../../common/elements/gamma-form";
import GammaFormField from "../../../../common/elements/gamma-form-field";

import { Center, MarginTop } from "../../../../common-ui/layout";
import {
  GammaCard,
  GammaCardBody,
  GammaCardButtons,
  GammaCardSubTitle,
  GammaCardTitle
} from "../../../../common-ui/design";

const InputCid = ({ sendCid, text }) => (
  <MarginTop>
    <Center>
      <GammaForm
        validationSchema={yup.object().shape({
          cid: yup.string().required(text.FieldRequired)
        })}
        initialValues={{ cid: "" }}
        onSubmit={(values, actions) => {
          actions.resetForm();
          sendCid(values);
        }}
        render={({ errors, touched }) => (
          <GammaCard absWidth="300px" absHeight="300px" hasSubTitle>
            <GammaCardTitle text={text.EnterYourCid} />
            <GammaCardSubTitle text={text.EnterYourCidDescription} />
            <GammaCardBody>
              <Center>
                <GammaFormField
                  name="cid"
                  component={CIDInput}
                  componentProps={{
                    upperLabel: text.Cid
                  }}
                />
              </Center>
            </GammaCardBody>
            <GammaCardButtons reverseDirection>
              <GammaButton text={text.SendCid} primary raised submit />
            </GammaCardButtons>
          </GammaCard>
        )}
      />
    </Center>
  </MarginTop>
);

InputCid.propTypes = {
  text: PropTypes.object.isRequired,
  sendCid: PropTypes.func.isRequired
};

export default InputCid;
