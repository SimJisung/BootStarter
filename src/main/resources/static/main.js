/**
 * Created by simjisung on 2015. 9. 18..
 */
var Nav = React.createClass({
    render: function(){
        return (
            <nav className="navbar navbar-inverse navbar-fixed-top">
                <div className="container">
                    <div className="navbar-header">
                        <button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                            <span className="sr-only">Toggle navigation</span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                            <span className="icon-bar"></span>
                        </button>
                        <a className="navbar-brand" href="#">Project name</a>
                    </div>
                    <div id="navbar" className="collapse navbar-collapse">
                        <ul className="nav navbar-nav">
                            <li className="active"><a href="#">Home</a></li>
                            <li><a href="#about">About</a></li>
                            <li><a href="#contact">Contact</a></li>
                        </ul>

                        <ul className="nav navbar-nav navbar-right">
                            <li><a href="#signup">Sign Up</a></li>
                            <li><a href="#signin">Sign In</a></li>

                        </ul>
                    </div> {/*<!--/.nav-collapse -->*/}
                </div>
            </nav>

        );
    }

});



var Main = React.createClass({
    render : function(){
        return (
            <div>
            <Nav/>

            <div className="container">

                <div className ="starter-template">

                    <h1>Bootstrap starter template</h1>

                    <p className ="lead">Use this document as a way to quickly start any new project. <br/>
                        All you get is this text and a mostly barebones HTML document.
                    </p>
                </div>

            </div> {/*<!-- /.container -->*/}

            </div>

        );
    }

});

var Sign = React.createClass({
    render : function(){
        return (
            <div>
                <Nav/>

                <div className="container">

                    <div className ="starter-template">

                        <h1>Sign Up</h1>

                        <p className = "lead">Use this document as a way to quickly start any new project. <br/>
                            All you get is this text and a mostly barebones HTML document.
                        </p>
                    </div>

                </div> {/*<!-- /.container -->*/}

            </div>

        );

    }

});

var App = React.createClass({

    getInitialState : function(){
       return {
           route : window.location.hash.substr(1)
       }

    },
    componentDidMount : function(){
        window.addEventListener('hashChange',() => {
           this.setState({
               route: window.location.hash.substr(1)
           });

        });
    },

    render : function(){
        var  Child;
        switch (this.state.route){
            case 'main' : Child = Main; break;
            case 'signup' : Child = Sign; break;
            default : Child = Main; break;
        }
        return (<Child/>);
    }
});

//router .module ,webpack or brower...
React.render(<App/>, document.body);
