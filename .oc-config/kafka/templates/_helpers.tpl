{{- define "kafka.routeHost" -}}
{{- if .Values.routes.enabled -}}
{{- printf "kafka-external-%s.%s.apps.ocp-free-trial.com" .Release.Name .Release.Namespace }}
{{- else -}}
{{- "" -}}
{{- end -}}
{{- end -}}
