# skylark rule to generate a Junit4 TestSuite
# Assumes srcs are all .java Test files
# Assumes junit4 is already added to deps by the user.

_OUTPUT = """import org.junit.runners.Suite;
import org.junit.runner.RunWith;

@RunWith(Suite.class)
@Suite.SuiteClasses({%s})
public class %s {}
"""

_PREFIXES = ("org", "com", "edu")

def _SafeIndex(l, val):
    for i, v in enumerate(l):
        if val == v:
            return i
    return -1

def _AsClassName(fname):
    fname = [x.path for x in fname.files][0]
    toks = fname[:-5].split("/")
    findex = -1
    for s in _PREFIXES:
        findex = _SafeIndex(toks, s)
        if findex != -1:
            break
    if findex == -1:
        fail(
            "%s does not contain any of %s",
            fname,
            _PREFIXES,
        )
    return ".".join(toks[findex:]) + ".class"

def _impl(ctx):
    classes = ",".join(
        [_AsClassName(x) for x in ctx.attr.srcs],
    )
    ctx.file_action(output = ctx.outputs.out, content = _OUTPUT % (
        classes,
        ctx.attr.outname,
    ))

_GenSuite = rule(
    implementation = _impl,
    attrs = {
        "srcs": attr.label_list(allow_files = True),
        "outname": attr.string(),
    },
    outputs = {"out": "%{name}.java"},
)

def junit4_test(name, test_srcs, srcs = [], **kwargs):
    s_name = name + "TestSuite"
    j_name = s_name + ".java"
    _GenSuite(
        name = s_name,
        srcs = test_srcs,
        outname = s_name,
    )
    native.java_test(
        name = name,
        test_class = s_name,
        srcs = test_srcs + srcs + [":" + s_name],
        **kwargs
    )
