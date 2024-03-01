import {
    DigitHeader,
    DigitHeaderDrawer,
    DigitLoading,
    useDigitTranslations
} from "@cthit/react-digit-components";
import React, { useContext, useEffect } from "react";
import { Route, Switch, useLocation } from "react-router-dom";
import ActivationCodes from "../use-cases/activation-codes";
import CreateAccount from "../use-cases/create-account";
import FourOFour from "../use-cases/four-o-four";
import Gdpr from "../use-cases/gdpr";
import Groups from "../use-cases/groups";
import Home from "../use-cases/home";
import Posts from "../use-cases/posts";
import Users from "../use-cases/users";
import Clients from "../use-cases/clients";
import ApiKeys from "../use-cases/api-keys";
import Whitelist from "../use-cases/whitelist";
import translations from "../common/utils/translations/CommonTranslations";
import SuperGroups from "../use-cases/super-groups";
import Me from "../use-cases/me";
import ResetPassword from "../use-cases/reset-password";
import Drawer from "./views/drawer";
import Members from "../use-cases/members";
import GammaUserContext from "../common/context/GammaUser.context";
import FiveZeroZero from "./elements/five-zero-zero";
import About from "../use-cases/about";
import Authorities from "../use-cases/authorities/Authorities";

export const App = () => {
    const [user, update, [loading, error], ignore] = useContext(
        GammaUserContext
    );
    const [, , , setCommonTranslations] = useDigitTranslations(translations);
    const { pathname } = useLocation();

    const title = "Gamma";

    useEffect(() => {
        setCommonTranslations(translations);
        // translations can't change in run time since it's a json file
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    useEffect(() => {
        if (loading && !user) {
            update(
                !pathname.startsWith("/create-account") &&
                    (!pathname.startsWith("/reset-password") ||
                        pathname.startsWith("/reset-password/admin"))
            );
        } else if (
            pathname.startsWith("/create-account") ||
            pathname.startsWith("/reset-password")
        ) {
            ignore();
        }
    }, [loading, error, pathname, update, user, ignore]);

    const main = (
        <>
            {loading && (
                <DigitLoading loading alignSelf={"center"} margin={"auto"} />
            )}
            {error && <FiveZeroZero getMe={update} />}
            {!loading && !error && (
                <Switch>
                    <Route path="/authorities" component={Authorities} />
                    <Route path="/clients" component={Clients} />
                    <Route path="/users" component={Users} />
                    <Route path="/groups" component={Groups} />
                    <Route path="/create-account" component={CreateAccount} />
                    <Route path="/whitelist" component={Whitelist} />
                    <Route path="/posts" component={Posts} />
                    <Route
                        path="/activation-codes"
                        component={ActivationCodes}
                    />
                    <Route path="/gdpr" component={Gdpr} />
                    <Route path="/clients" component={Clients} />
                    <Route path="/access-keys" component={ApiKeys} />
                    <Route path="/super-groups" component={SuperGroups} />
                    <Route path={"/me"} component={Me} />
                    <Route path={"/about"} component={About} />
                    <Route path="/members" component={Members} />
                    <Route path="/reset-password" component={ResetPassword} />
                    <Route path="/" exact component={Home} />
                    <Route component={FourOFour} />
                </Switch>
            )}
        </>
    );

    const headerOptions = {
        title,
        headerHeight: "200px",
        backgroundImage: "url(/enbarsskar.jpg)",
        renderMain: () => main
    };

    if (user == null) {
        return <DigitHeader {...headerOptions} />;
    }

    return (
        <DigitHeaderDrawer
            {...headerOptions}
            renderDrawer={closeDrawer =>
                user == null ? null : <Drawer closeDrawer={closeDrawer} />
            }
        />
    );
};

export default App;
