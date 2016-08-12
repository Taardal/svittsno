# Encoding: utf-8

# Script that deploys the application to a remote server. Depends on Ruby and net-ssh og net-scp rubygems. Ex: 'gem install net-ssh'
# Run 'rake -T' for available tasks

# Running a task will execute the following:
# - Build svitts core module (.war)
# - Stop tomcat on remote server and remove old version (folder and .war-file)
# - Copy new svitts.war (core) to remote server
# - Start tomcat on remote server
# - Ensure that SvittsNO webapp is running by pinging REST API self-test.


require 'net/ssh'
require 'net/scp'
require 'date'

namespace :default do
  puts ''
  puts '-------------------------------------------------------'
  puts ' AVAILABLE RAKE TASKS'
  puts '-------------------------------------------------------'
  puts ' - rake deploy:test'
  puts ' - rake deploy:svitts'
  puts '-------------------------------------------------------'
  puts ''
end

today = Date.today

api_version = 'v1'

build_profile = ''
server_user = ''
server_address = ''
server_tomcat_port = ''
server_tomcat_catalina_script = ''
server_tomcat_webapps_path = ''

test_build_profile = 'test'
test_server_user = 'taardal'
test_server_address = '172.16.42.19'
test_tomcat_port = '8080'
test_tomcat_catalina_base = '/var/lib/tomcat8'
test_tomcat_catalina_home = '/usr/share/tomcat8'

prod_build_profile = 'prod'
prod_server_user = ''
prod_server_address = ''
prod_tomcat_port = ''
prod_tomcat_catalina_base = ''
prod_tomcat_catalina_home = ''

namespace :deploy do
  # task :test => [:set_test_as_server, :build, :cleanup_server, :deploy, :start_server, :ping] do
  task :test => [:set_test_as_server, :build, :cleanup_server, :deploy, :start_server] do
    puts 'Done'
  end
  # task :prod => [:set_prod_as_server, :build, :cleanup_server, :deploy, :start_server, :ping] do
  task :prod => [:set_prod_as_server, :build] do
    puts 'Done'
  end
end

task :set_test_as_server do
  build_profile = test_build_profile
  server_user = test_server_user
  server_address = test_server_address
  server_tomcat_port = test_tomcat_port
  server_tomcat_webapps_path = test_tomcat_catalina_base + '/webapps'
  server_tomcat_catalina_script = test_tomcat_catalina_home + '/bin/catalina.sh'
end

task :set_prod_as_server do
  build_profile = prod_build_profile
  server_user = prod_server_user
  server_address = prod_server_address
  server_tomcat_port = prod_tomcat_port
  server_tomcat_webapps_path = prod_tomcat_catalina_base
  server_tomcat_catalina_script = prod_tomcat_catalina_home + '/bin/catalina.sh'
end

task :build do
  puts "Building SvittsNO (core) with profile #{build_profile} using Maven..."
  puts ''
  puts %x{mvn clean package -P test}
  puts ''
  raise 'SvittsNO (core) build failed!' if $?.exitstatus != 0
end

task :cleanup_server do
  puts "Shutting down and cleaning up #{build_profile} server (#{server_address})..."
  Net::SSH.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |ssh|
    ssh.exec!("#{server_tomcat_catalina_script} stop")
    ssh.exec!("rm -rf #{server_tomcat_webapps_path}/svitts.war")
    ssh.exec!("rm -rf #{server_tomcat_webapps_path}/svitts")
  end
end

task :deploy do
  puts "Deploying SvittsNO (core) to #{build_profile} server (#{server_address})..."
  Net::SCP.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |scp|
    scp.upload!("target/svitts.war", "#{server_tomcat_webapps_path}")
  end
  Net::SSH.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |ssh|
    ssh.exec!("chmod 775 #{server_tomcat_webapps_path}/svitts.war")
  end
end

task :start_server do
  puts "Starting #{build_profile} server (#{server_address})..."
  Net::SSH.start(
      "#{server_address}",
      "#{server_user}",
      :auth_methods => ['publickey']
  ) do |ssh|
    ssh.exec!("#{server_tomcat_catalina_script} start")
  end
end

task :ping do
  puts "Is SvittsNO up on #{build_profile} server (#{server_address})?"
  time_spent = 0
  response = 0
  while time_spent < 30
    response = %x{curl -s -o /dev/null -w "%{http_code}" 'http://#{server_address}:#{server_tomcat_port}/svitts/api/#{api_version}/selftest'}
    if response == '200'
      break
    end
    sleep(1)
    print "."
    $stdout.flush
    time_spent += 1
  end

  if response != '200'
    fail "SvittsNO is not up (#{response})"
  end

  puts ""
  puts "HTTP response from SvittsNO: #{response}"
end