import {
    DigitFAB,
    DigitTable,
    DigitTranslations,
    DigitDesign
} from "@cthit/react-digit-components";
import { Add } from "@material-ui/icons";
import React from "react";
import { Fill } from "../../../../common-ui/layout";
import translations from "./ShowAllPosts.screen.translations.json";

const ShowAllPosts = ({ posts }) => (
    <DigitTranslations
        translations={translations}
        uniquePath="Posts.Screen.ShowAllPosts"
        render={text => (
            <Fill>
                <DigitTable
                    titleText={text.Posts}
                    searchText={text.SearchForPosts}
                    idProp="id"
                    startOrderBy="sv"
                    columnsOrder={["id", "sv", "en"]}
                    headerTexts={{
                        id: text.Id,
                        sv: text.Swedish,
                        en: text.English,
                        __link: text.Details
                    }}
                    data={posts.map(post => {
                        return {
                            ...post,
                            __link: "/posts/" + post.id
                        };
                    })}
                    emptyTableText={text.NoPosts}
                />
                <DigitDesign.Link to="/posts/add">
                    <DigitFAB icon={Add} secondary />
                </DigitDesign.Link>
            </Fill>
        )}
    />
);

export default ShowAllPosts;
