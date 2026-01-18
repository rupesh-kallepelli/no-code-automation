import React, { useState, useEffect, useRef } from "react";

function LiveCanvas({ wsUrl, format = "jpeg", start }) {
  const canvasRef = useRef(null);

  useEffect(() => {
    if (!start) return; // don't connect until start is true

    const ws = new WebSocket(wsUrl);

    ws.onopen = () => console.log("WebSocket connected");
    ws.onerror = (err) => console.error("WebSocket error", err);
    ws.onclose = () => console.log("WebSocket closed");

    ws.onmessage = (event) => {
      const img = new Image();
      img.src = `data:image/${format};base64,${event.data}`;
      img.onload = () => {
        const canvas = canvasRef.current;
        if (!canvas) return;

        canvas.width = 1920;  // internal canvas resolution width
        canvas.height = 1080; // internal canvas resolution height

        const ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

      };
    };

    return () => ws.close();
  }, [wsUrl, format, start]);

  return (
    <canvas
      ref={canvasRef}
      width={1920}
      height={1080}
      style={{
        width: "100%",      // scale display width to container
        height: "auto",     // maintain aspect ratio
        border: "1px solid #ccc",
        marginTop: 20,
        backgroundColor: "#000",
        display: "block",
      }}
    />

  );
}

export default function App() {
  const [testCase, setTestCase] = useState("");
  const [result, setResult] = useState("");
  const [startCasting, setStartCasting] = useState(false);

  const runTest = async () => {
    try {
      setStartCasting(true); // start the WebSocket only now
//      const res = await fetch("https://no-code-ai-automation-kallepallirupesh-dev.apps.rm2.thpm.p1.openshiftapps.com/api/run-test", {
      const res = await fetch("http://localhost:8080/api/run-test", {
        method: "POST",
        headers: { "Content-Type": "text/plain" },
        body: testCase,
      });
      setResult(await res.text());
    } catch (error) {
      setResult("Error running test: " + error.message);
    }
  };

  return (
    <div style={{ padding: 20, maxWidth: 1200, margin: "0 auto" }}>
      <h2>No-Code AI Test Runner</h2>

      <textarea
        rows={10}
        style={{ width: "100%", fontSize: 16 }}
        placeholder="Write test case in plain English..."
        value={testCase}
        onChange={(e) => setTestCase(e.target.value)}
      />

      <button onClick={runTest} style={{ marginTop: 10, padding: "8px 16px" }}>
        Run Test
      </button>

      <pre style={{ whiteSpace: "pre-wrap", marginTop: 10 }}>{result}</pre>

      <div style={{ marginTop: 40 }}>
        <h3>Live Screencast</h3>
        <LiveCanvas
          wsUrl="ws://localhost:8080/ws/screencast"
          format="jpeg"
          start={startCasting}
        />
      </div>
    </div>
  );
}
