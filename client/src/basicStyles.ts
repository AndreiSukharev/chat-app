import {makeStyles} from "@material-ui/core/styles";

const basicStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    input: {
        marginTop: theme.spacing(2),
    },
}));

export default basicStyles;