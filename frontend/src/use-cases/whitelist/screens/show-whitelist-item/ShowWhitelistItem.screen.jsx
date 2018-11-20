import React from "react";
import styled from "styled-components";
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
import GammaDisplayData from "../../../../common/elements/gamma-display-data";
import { DigitTranslations, DigitButton } from "@cthit/react-digit-components";
import translations from "./ShowWhitelistItem.screen.translations.json";

const ShowWhitelistItem = ({
    whitelistItem,
    whitelistDelete,
    redirectTo,
    toastOpen,
    gammaDialogOpen
}) => (
    <IfElseRendering
        test={whitelistItem != null}
        ifRender={() => (
            <DigitTranslations
                translations={translations}
                uniquePath="Whitelist.Screen.ShowWhitelistItem"
                render={text => (
                    <Fill>
                        <Center>
                            <GammaCard minWidth="300px" maxWidth="600px">
                                <GammaCardBody>
                                    <GammaDisplayData
                                        data={whitelistItem}
                                        keysOrder={["id", "cid"]}
                                        keysText={{
                                            id: text.Id,
                                            cid: text.Cid
                                        }}
                                    />
                                </GammaCardBody>
                                <GammaCardButtons reversedDirection>
                                    <DigitButton
                                        text={text.DeleteWhitelistItem}
                                        onClick={() => {
                                            gammaDialogOpen({
                                                title:
                                                    text.WouldYouLikeToDelete +
                                                    " " +
                                                    whitelistItem.cid +
                                                    "?",
                                                confirmButtonText:
                                                    text.DeleteWhitelistItem,
                                                cancelButtonText: text.Cancel,
                                                onConfirm: () => {
                                                    whitelistDelete(
                                                        whitelistItem.id
                                                    )
                                                        .then(response => {
                                                            toastOpen({
                                                                text:
                                                                    text.DeleteSuccessfully +
                                                                    " " +
                                                                    whitelistItem.cid
                                                            });
                                                            redirectTo(
                                                                "/whitelist"
                                                            );
                                                        })
                                                        .catch(error => {
                                                            toastOpen({
                                                                text:
                                                                    text.SomethingWentWrong
                                                            });
                                                        });
                                                }
                                            });
                                        }}
                                    />
                                    <Spacing />
                                    <GammaLink
                                        to={
                                            "/whitelist/" +
                                            whitelistItem.id +
                                            "/edit"
                                        }
                                    >
                                        <DigitButton
                                            text={text.EditWhitelistItem}
                                            primary
                                            raised
                                        />
                                    </GammaLink>
                                </GammaCardButtons>
                            </GammaCard>
                        </Center>
                    </Fill>
                )}
            />
        )}
    />
);

export default ShowWhitelistItem;
