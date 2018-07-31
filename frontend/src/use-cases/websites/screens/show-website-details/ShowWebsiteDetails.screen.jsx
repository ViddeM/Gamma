import React from "react";
import { Fill, Center, Spacing } from "../../../../common-ui/layout";

import {
  GammaCard,
  GammaCardTitle,
  GammaCardBody,
  GammaLink,
  GammaCardButtons
} from "../../../../common-ui/design";
import IfElseRendering from "../../../../common/declaratives/if-else-rendering";
import { Text } from "../../../../common-ui/text";
import GammaButton from "../../../../common/elements/gamma-button";
import { Edit } from "@material-ui/icons";
import GammaDisplayData from "../../../../common/elements/gamma-display-data/GammaDisplayData.element";
import GammaTranslations from "../../../../common/declaratives/gamma-translations";
import translations from "./ShowWebsiteDetails.screen.translations.json";

const ShowWebsiteDetails = ({
  website,
  gammaDialogOpen,
  toastOpen,
  redirectTo,
  websitesDelete
}) => (
  <IfElseRendering
    test={website != null}
    ifRender={() => (
      <GammaTranslations
        translations={translations}
        uniquePath="Websites.Screen.ShowWebsiteDetails"
        render={text => (
          <Fill>
            <Center>
              <GammaCard minWidth="300px" maxWidth="600px">
                <GammaCardBody>
                  <GammaDisplayData
                    data={website}
                    keysText={{
                      id: text.Id,
                      name: text.Name,
                      prettyName: text.PrettyName
                    }}
                    keysOrder={["id", "name", "prettyName"]}
                  />
                </GammaCardBody>
                <GammaCardButtons reverseDirection>
                  <GammaLink to={"/websites/" + website.id + "/edit"}>
                    <GammaButton text={text.EditWebsite} primary raised />
                  </GammaLink>
                  <GammaButton
                    onClick={() => {
                      gammaDialogOpen({
                        title:
                          text.WouldYouLikeToDelete + " " + website.prettyName,
                        confirmButtonText: text.DeleteWebsite,
                        cancelButtonText: text.Cancel,
                        onConfirm: () => {
                          websitesDelete(website.id)
                            .then(response => {
                              toastOpen({
                                text: website.prettyName + " " + text.Deleted
                              });
                              redirectTo("/websites");
                            })
                            .catch(error => {
                              toastOpen({
                                text: text.SomethingWentWrong
                              });
                            });
                        }
                      });
                    }}
                    text={text.DeleteWebsite}
                  />
                </GammaCardButtons>
              </GammaCard>
            </Center>
          </Fill>
        )}
      />
    )}
  />
);

export default ShowWebsiteDetails;
